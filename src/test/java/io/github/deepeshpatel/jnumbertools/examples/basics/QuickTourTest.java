/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.basics;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickTourTest {

    @Test
    void reportsKnownCounts() {
        List<String> lines = QuickTour.lines();
        assertEquals(8, lines.size());
        assertTrue(lines.get(0).endsWith(": 24"));
        assertTrue(lines.get(2).endsWith(": 2432902008176640000"));
        assertTrue(lines.get(4).endsWith(": 2598960"));
        assertTrue(lines.get(5).endsWith(": 8"));
        assertTrue(lines.get(6).endsWith(": 9"));
        assertTrue(lines.get(7).endsWith(": 6"));
    }
}
