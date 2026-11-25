package io.github.higor96.obsi;

import io.github.higor96.obsi.input.InputError;
import io.github.higor96.obsi.input.Snapshot;
import io.github.higor96.obsi.input.SnapshotReader;
import io.github.higor96.obsi.method.CalculationFailure;
import io.github.higor96.obsi.method.ObsiCalculator;
import io.github.higor96.obsi.method.Publication;
import io.github.higor96.obsi.output.ResultJson;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Command-line entry point for the OBSI calculation.
 *
 * <p>Usage: {@code ./run.sh [--new-ped] <input.json>}. A published result is one JSON object
 * on stdout with exit 0. Any failure is one JSON object on stderr with exit 1, or exit 2 for a
 * usage error (D-008). Nothing else is printed on either stream.
 */
public final class Main {

    static final int PUBLISHED = 0;
    static final int FAILED = 1;
    static final int USAGE_ERROR = 2;

    private static final String USAGE = "usage: ./run.sh [--new-ped] <input.json>";

    private Main() {
    }

    public static void main(String[] args) {
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8);
        System.exit(run(args, out, err));
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        List<String> paths = new ArrayList<>();
        boolean newPed = false;
        for (String arg : args) {
            if (arg.equals("--new-ped")) {
                newPed = true;
            } else if (arg.startsWith("--")) {
                return fail(err, Optional.empty(), "unknown option " + arg + "; " + USAGE, USAGE_ERROR);
            } else {
                paths.add(arg);
            }
        }
        if (paths.size() != 1) {
            return fail(err, Optional.empty(), "expected one input file; " + USAGE, USAGE_ERROR);
        }
        if (newPed) {
            return fail(err, Optional.empty(), "--new-ped is not implemented yet", USAGE_ERROR);
        }
        try {
            return calculate(Path.of(paths.getFirst()), out, err);
        } catch (RuntimeException e) {
            // A bug, not a property of the snapshot: still one JSON object on stderr, never a stack trace.
            return fail(err, Optional.empty(), "internal error: " + e, FAILED);
        }
    }

    private static int calculate(Path input, PrintStream out, PrintStream err) {
        String json;
        try {
            json = Files.readString(input);
        } catch (NoSuchFileException e) {
            return fail(err, Optional.empty(), "cannot read input file " + input + ": no such file", FAILED);
        } catch (IOException e) {
            return fail(err, Optional.empty(),
                    "cannot read input file " + input + ": " + e.getClass().getSimpleName(), FAILED);
        }

        Snapshot snapshot;
        try {
            snapshot = SnapshotReader.read(json);
        } catch (InputError e) {
            return fail(err, e.calculationTime(), e.getMessage(), FAILED);
        }

        try {
            Publication publication = ObsiCalculator.calculate(snapshot);
            out.print(ResultJson.published(snapshot.calculationTime(), publication) + "\n");
            return PUBLISHED;
        } catch (CalculationFailure e) {
            return fail(err, Optional.of(snapshot.calculationTime()), e.getMessage(), FAILED);
        }
    }

    private static int fail(PrintStream err, Optional<String> calculationTime, String reason, int exitCode) {
        err.print(ResultJson.failed(calculationTime, reason) + "\n");
        return exitCode;
    }
}
