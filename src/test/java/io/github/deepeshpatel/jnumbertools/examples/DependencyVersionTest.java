/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Proves that the JNumberTools used here is the published jar of the version declared
 * in {@code pom.xml}, not a locally compiled copy.
 */
class DependencyVersionTest {

    @Test
    void libraryComesFromTheJarOfTheDeclaredVersion() throws URISyntaxException {
        String expected = System.getProperty("jnumbertools.version");
        assertNotNull(expected, "jnumbertools.version must be passed by surefire (see pom.xml)");

        var location = JNumberTools.class.getProtectionDomain().getCodeSource().getLocation();
        Path jar = Path.of(location.toURI());

        assertTrue(jar.toString().endsWith(".jar"), "JNumberTools must be loaded from a jar, was " + jar);
        assertEquals("jnumbertools-" + expected + ".jar", jar.getFileName().toString());
    }

    @Test
    void libraryWorksThroughThePublicApi() {
        assertEquals("24", JNumberTools.permutations().unique(4).count().toString());
        assertEquals("2598960", JNumberTools.combinations().unique(52, 5).count().toString());
        assertEquals("9", JNumberTools.derangements().of(4).count().toString());
    }
}
