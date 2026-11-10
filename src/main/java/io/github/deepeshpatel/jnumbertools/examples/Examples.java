/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import io.github.deepeshpatel.jnumbertools.examples.basics.QuickTour;
import io.github.deepeshpatel.jnumbertools.examples.bio.DnaMotifs;
import io.github.deepeshpatel.jnumbertools.examples.config.FeatureFlagTesting;
import io.github.deepeshpatel.jnumbertools.examples.experiments.AbTestDesign;
import io.github.deepeshpatel.jnumbertools.examples.games.PokerHands;
import io.github.deepeshpatel.jnumbertools.examples.groups.SecretSanta;
import io.github.deepeshpatel.jnumbertools.examples.hr.CommitteeSelection;
import io.github.deepeshpatel.jnumbertools.examples.logistics.DeliveryRoute;
import io.github.deepeshpatel.jnumbertools.examples.lottery.LotteryOdds;
import io.github.deepeshpatel.jnumbertools.examples.security.KeyspacePartitioning;
import io.github.deepeshpatel.jnumbertools.examples.storage.CompactPermutationStorage;
import io.github.deepeshpatel.jnumbertools.examples.testing.CrossPlatformTestMatrix;

import java.util.List;

/**
 * Registry of every runnable example. Add new examples here.
 */
public final class Examples {

    private static final List<Example> ALL = List.of(
            new Example("quick-tour", "basics",
                    "One call per generator family, with counts too large to enumerate",
                    QuickTour::run),
            new Example("test-matrix", "testing",
                    "Browser x OS x network test matrix with compatibility rules and a deterministic smoke subset",
                    CrossPlatformTestMatrix::run),
            new Example("keyspace-partitioning", "security",
                    "Split a 218-trillion-candidate password space between workers without enumerating it",
                    KeyspacePartitioning::run),
            new Example("secret-santa", "groups",
                    "Gift draw where nobody gets themselves, sampled directly from the derangements",
                    SecretSanta::run),
            new Example("lottery-odds", "lottery",
                    "6/49 odds, and a ticket turned into its rank and back",
                    LotteryOdds::run),
            new Example("deck-storage", "storage",
                    "A shuffled 52-card deck stored as one integer and restored exactly",
                    CompactPermutationStorage::run),
            new Example("dna-motifs", "bio",
                    "All DNA words of length k, and every distinct rearrangement of a motif without duplicates",
                    DnaMotifs::run),
            new Example("delivery-route", "logistics",
                    "Shortest round trip for a van by trying every visiting order",
                    DeliveryRoute::run),
            new Example("feature-flags", "config",
                    "Flag configurations with at most two flags on, sized by range, with a dependency rule",
                    FeatureFlagTesting::run),
            new Example("poker-hands", "games",
                    "Enumerate all 2,598,960 five-card hands and reproduce the textbook odds table",
                    PokerHands::run),
            new Example("committee-selection", "hr",
                    "Committee with a fixed role mix from three pools, counted, listed and sampled",
                    CommitteeSelection::run),
            new Example("ab-test-design", "experiments",
                    "18 landing-page variants: stable per-visitor assignment by rank, and a seeded pilot set",
                    AbTestDesign::run)
    );

    private Examples() {
    }

    /** @return all registered examples in registration order */
    public static List<Example> all() {
        return ALL;
    }
}
