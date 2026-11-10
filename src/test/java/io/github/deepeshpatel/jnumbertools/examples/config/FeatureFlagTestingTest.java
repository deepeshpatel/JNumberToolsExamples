/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeatureFlagTestingTest {

    @Test
    void countsBySizeRange() {
        assertEquals(32, FeatureFlagTesting.allConfigurations());
        assertEquals(1 + 5 + 10, FeatureFlagTesting.configurationsWithFlagsOn(0, 2));
        assertEquals(5 + 10, FeatureFlagTesting.configurationsWithFlagsOn(1, 2));
        assertEquals(10, FeatureFlagTesting.configurationsWithFlagsOn(2, 2));
    }

    @Test
    void pairwiseConfigurationsRespectTheDependencyRule() {
        var configs = FeatureFlagTesting.pairwiseConfigurations();
        assertEquals(12, configs.size());
        assertEquals(List.of(), configs.getFirst());
        assertTrue(configs.contains(List.of("newCheckout", "giftWrap")));
        assertFalse(configs.contains(List.of("giftWrap")));
        assertTrue(configs.stream().allMatch(c -> c.size() <= 2 && FeatureFlagTesting.isValid(c)));
    }

    @Test
    void everyFlagAndEveryAllowedPairAppearsAtLeastOnce() {
        var configs = FeatureFlagTesting.pairwiseConfigurations();
        for (String a : FeatureFlagTesting.FLAGS) {
            if (!a.equals("giftWrap")) {
                assertTrue(configs.stream().anyMatch(c -> c.contains(a)), a);
            }
        }
        assertTrue(configs.stream().anyMatch(c -> c.containsAll(List.of("darkMode", "betaSearch"))));
    }
}
