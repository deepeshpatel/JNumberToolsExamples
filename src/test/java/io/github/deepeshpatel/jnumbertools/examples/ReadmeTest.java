/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Keeps the README in step with the registry: every example must be documented. */
class ReadmeTest {

    @Test
    void readmeMentionsEveryRegisteredExample() throws IOException {
        String readme = Files.readString(Path.of("README.md"));
        for (Example e : Examples.all()) {
            assertTrue(readme.contains("`" + e.id() + "`"), "README.md does not mention example " + e.id());
        }
    }

    @Test
    void readmeNamesTheDeclaredLibraryVersion() throws IOException {
        String readme = Files.readString(Path.of("README.md"));
        String version = System.getProperty("jnumbertools.version");
        assertTrue(readme.contains("<version>" + version + "</version>"),
                "README.md dependency snippet must use version " + version);
    }
}
