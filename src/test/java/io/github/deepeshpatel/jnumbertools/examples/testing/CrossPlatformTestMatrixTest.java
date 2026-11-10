/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.testing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrossPlatformTestMatrixTest {

    @Test
    void fullProductIsBrowsersTimesSystemsTimesNetworks() {
        assertEquals(4 * 5 * 2, CrossPlatformTestMatrix.fullSize());
    }

    @Test
    void supportedCombinationsObeyTheRules() {
        var supported = CrossPlatformTestMatrix.supported();
        // Chrome 5 + Firefox 5 + Safari 2 + Edge 2 = 14 browser/system pairs, times 2 networks
        assertEquals(28, supported.size());
        assertFalse(supported.contains(List.of("Safari", "Windows", "fast")));
        assertFalse(supported.contains(List.of("Edge", "Linux", "slow-3g")));
        assertTrue(supported.contains(List.of("Safari", "iOS", "slow-3g")));
    }

    @Test
    void smokeSubsetIsDeterministicAndSupported() {
        var subset = CrossPlatformTestMatrix.smokeSubset(4);
        assertEquals(subset, CrossPlatformTestMatrix.smokeSubset(4));
        assertEquals(7, subset.size());
        assertEquals(List.of("Chrome", "Windows", "fast"), subset.getFirst());
        assertEquals(List.of("Edge", "macOS", "fast"), subset.getLast());
        assertTrue(CrossPlatformTestMatrix.supported().containsAll(subset));
    }
}
