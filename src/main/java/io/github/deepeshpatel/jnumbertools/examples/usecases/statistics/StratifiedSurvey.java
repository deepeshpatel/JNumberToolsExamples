/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.statistics;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.product.constrained.ConstrainedProductBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stratified survey sampling.
 * <p>
 * Real-life problem: a company of 80 people (40 in engineering, 25 in sales, 15 in support) wants a
 * 16-person survey that mirrors the company: 8, 5 and 3 people from the three groups. A simple random
 * sample of 16 usually gets the mix wrong by chance. A stratified sample fixes the mix and still picks
 * randomly inside each group. That is a Cartesian product of three selections ("8 distinct of 40",
 * "5 distinct of 25", "3 distinct of 15"), so the constrained product builder draws it directly,
 * without listing the tens of quadrillions of possible samples.
 * <p>
 * A stratified sample is a flat list: the engineers first, then sales, then support.
 */
public final class StratifiedSurvey {

    static final String[] STRATA = {"ENG", "SALES", "SUPPORT"};
    static final int[] SIZES = {40, 25, 15};
    static final int[] QUOTAS = {8, 5, 3};
    static final int SAMPLE_SIZE = 16;

    private StratifiedSurvey() {
    }

    /** @return the people of one stratum, such as ENG-01 .. ENG-40 */
    static List<String> members(int stratum) {
        List<String> people = new ArrayList<>();
        for (int i = 1; i <= SIZES[stratum]; i++) {
            people.add(String.format("%s-%02d", STRATA[stratum], i));
        }
        return people;
    }

    /** @return all 80 people, stratum by stratum */
    static List<String> everyone() {
        List<String> all = new ArrayList<>();
        for (int s = 0; s < STRATA.length; s++) {
            all.addAll(members(s));
        }
        return all;
    }

    private static ConstrainedProductBuilder stratified() {
        return JNumberTools.cartesianProduct()
                .constrainedProductOfDistinct(QUOTAS[0], members(0))
                .andDistinct(QUOTAS[1], members(1))
                .andDistinct(QUOTAS[2], members(2));
    }

    /** @return number of possible stratified samples, C(40,8) x C(25,5) x C(15,3) */
    public static BigInteger stratifiedSampleSpace() {
        return stratified().count();
    }

    /** @return number of possible simple random samples, C(80,16) */
    public static BigInteger simpleSampleSpace() {
        return JNumberTools.combinations().unique(SAMPLE_SIZE, everyone()).count();
    }

    /** @return a reproducible stratified sample: exactly the quota from every stratum */
    public static List<Object> stratifiedSample(long seed) {
        return stratified().sample(1, new Random(seed)).iterator().next();
    }

    /** @return how many people of each stratum a sample contains, judged by the id prefix */
    public static int[] stratumCounts(List<?> sample) {
        int[] counts = new int[STRATA.length];
        for (Object person : sample) {
            for (int s = 0; s < STRATA.length; s++) {
                if (person.toString().startsWith(STRATA[s] + "-")) {
                    counts[s]++;
                }
            }
        }
        return counts;
    }

    /** @return the fewest and the most engineers found in {@code draws} simple random samples of 16 */
    public static int[] engineerSpreadInSimpleSamples(int draws, long seed) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (List<String> sample : JNumberTools.combinations().unique(SAMPLE_SIZE, everyone())
                .choice(draws, new Random(seed))) {
            int engineers = stratumCounts(sample)[0];
            min = Math.min(min, engineers);
            max = Math.max(max, engineers);
        }
        return new int[]{min, max};
    }

    public static void run() {
        System.out.println("Company: 40 engineering, 25 sales, 15 support = 80 people; survey of 16 (8 + 5 + 3)");
        System.out.println("Possible simple random samples: " + simpleSampleSpace());
        System.out.println("Possible stratified samples   : " + stratifiedSampleSpace());
        System.out.println();

        List<Object> sample = stratifiedSample(42);
        int[] counts = stratumCounts(sample);
        System.out.println("A stratified sample (seed 42): " + sample);
        System.out.println("  engineering " + counts[0] + ", sales " + counts[1] + ", support " + counts[2]);

        int[] spread = engineerSpreadInSimpleSamples(1000, 42);
        System.out.println();
        System.out.println("Engineers in 1000 simple random samples: from " + spread[0] + " to " + spread[1]
                + " (the fair share is 8)");
        System.out.println("Engineers in every stratified sample    : exactly 8");
    }

    public static void main(String[] args) {
        run();
    }
}
