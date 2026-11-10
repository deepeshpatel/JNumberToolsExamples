/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExamplesSmokeTest {

    @Test
    void registryIsNotEmptyAndIdsAreUnique() {
        var examples = Examples.all();
        assertFalse(examples.isEmpty());
        var ids = new HashSet<String>();
        for (Example e : examples) {
            assertTrue(ids.add(e.id()), "duplicate example id: " + e.id());
        }
    }

    @Test
    void everyExampleRunsAndPrintsSomething() {
        for (Example e : Examples.all()) {
            String output = capture(e.runner());
            assertFalse(output.isBlank(), e.id() + " printed nothing");
        }
    }

    @Test
    void mainRunsAllExamples() {
        String output = capture(() -> Main.main(new String[]{"all"}));
        for (Example e : Examples.all()) {
            assertTrue(output.contains("=== " + e.id()), "missing section for " + e.id());
        }
    }

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        var buffer = new ByteArrayOutputStream();
        try (var replacement = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(replacement);
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
