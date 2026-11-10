/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command-line runner.
 * <ul>
 *   <li>no arguments: list the examples</li>
 *   <li>{@code all} (alone or among other arguments): run every example</li>
 *   <li>one or more ids: run those examples</li>
 * </ul>
 * An unknown id prints a one-line message to standard error and exits with status 1 before
 * anything is run.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        int status = execute(args);
        if (status != 0) {
            System.exit(status);
        }
    }

    /** @return the process exit status: 0 on success, 1 if an id is unknown */
    static int execute(String[] args) {
        List<Example> examples = Examples.all();
        if (args.length == 0) {
            list(examples);
            return 0;
        }

        List<Example> selected = new ArrayList<>();
        if (Arrays.asList(args).contains("all")) {
            selected.addAll(examples);
        } else {
            for (String id : args) {
                Example example = examples.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
                if (example == null) {
                    System.err.println("Unknown example '" + id + "'. Run without arguments to list them.");
                    return 1;
                }
                selected.add(example);
            }
        }
        selected.forEach(Main::run);
        return 0;
    }

    private static void list(List<Example> examples) {
        System.out.println("Available examples (run with: mvn -q compile exec:java -Dexec.args=\"<id>\" or \"all\"):");
        for (Example e : examples) {
            System.out.printf("  %-24s [%s] %s%n", e.id(), e.domain(), e.summary());
        }
    }

    private static void run(Example example) {
        System.out.println("=== " + example.id() + " [" + example.domain() + "] ===");
        System.out.println(example.summary());
        System.out.println();
        example.runner().run();
        System.out.println();
    }
}
