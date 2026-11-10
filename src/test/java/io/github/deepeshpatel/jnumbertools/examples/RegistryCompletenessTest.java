/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Catches an example class that was written but never registered: every class under the examples
 * packages that declares {@code public static void run()} must be referenced as {@code Name::run}
 * in {@code Examples.java}.
 */
class RegistryCompletenessTest {

    private static final Path SOURCES = Path.of("src/main/java/io/github/deepeshpatel/jnumbertools/examples");

    @Test
    void everyClassWithARunMethodIsRegistered() throws IOException {
        String registry = Files.readString(SOURCES.resolve("Examples.java"));
        List<String> unregistered = new ArrayList<>();
        try (Stream<Path> files = Files.walk(SOURCES)) {
            for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                String name = file.getFileName().toString().replace(".java", "");
                if (Files.readString(file).contains("public static void run()") && !registry.contains(name + "::run")) {
                    unregistered.add(name);
                }
            }
        }
        assertTrue(unregistered.isEmpty(), "Not registered in Examples.java: " + unregistered);
    }
}
