/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private record Result(int status, String out, String err) {
    }

    @Test
    void noArgumentsListsEveryExample() {
        Result r = execute();
        assertEquals(0, r.status());
        for (Example e : Examples.all()) {
            assertTrue(r.out().contains(e.id()), "listing misses " + e.id());
        }
        assertTrue(r.out().contains("compile exec:java"), "help text must include the compile phase");
    }

    @Test
    void anIdRunsOnlyThatExample() {
        Result r = execute("quick-tour");
        assertEquals(0, r.status());
        assertTrue(r.out().contains("=== quick-tour"));
        assertFalse(r.out().contains("=== secret-santa"));
    }

    @Test
    void unknownIdIsReportedWithoutRunningAnything() {
        Result r = execute("quick-tour", "no-such-example");
        assertEquals(1, r.status());
        assertTrue(r.err().contains("Unknown example 'no-such-example'"));
        assertEquals("", r.out());
    }

    @Test
    void allAmongOtherArgumentsRunsEverything() {
        Result r = execute("quick-tour", "all");
        assertEquals(0, r.status());
        for (Example e : Examples.all()) {
            assertTrue(r.out().contains("=== " + e.id()), "missing section for " + e.id());
        }
    }

    private static Result execute(String... args) {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();
        try (var o = new PrintStream(out, true, StandardCharsets.UTF_8);
             var e = new PrintStream(err, true, StandardCharsets.UTF_8)) {
            System.setOut(o);
            System.setErr(e);
            int status = Main.execute(args);
            return new Result(status, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
