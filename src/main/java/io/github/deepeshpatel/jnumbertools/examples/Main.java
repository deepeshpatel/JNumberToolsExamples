/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import java.util.List;

/**
 * Command-line runner.
 * <ul>
 *   <li>no arguments: list the examples</li>
 *   <li>{@code all}: run every example</li>
 *   <li>one or more ids: run those examples</li>
 * </ul>
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        List<Example> examples = Examples.all();
        if (args.length == 0) {
            list(examples);
            return;
        }
        if (args.length == 1 && args[0].equals("all")) {
            examples.forEach(Main::run);
            return;
        }
        for (String id : args) {
            Example example = examples.stream()
                    .filter(e -> e.id().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown example '" + id + "'. Run without arguments to list them."));
            run(example);
        }
    }

    private static void list(List<Example> examples) {
        System.out.println("Available examples (run with: mvn -q exec:java -Dexec.args=\"<id>\" or \"all\"):");
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
