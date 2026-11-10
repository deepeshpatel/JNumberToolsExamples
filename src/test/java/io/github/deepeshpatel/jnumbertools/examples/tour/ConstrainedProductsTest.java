/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstrainedProductsTest {

    private static List<List<Object>> allPizzas() {
        return ConstrainedProducts.first(1000);
    }

    @Test
    void countMatchesTheFormulaAndTheNumberOfListedPizzas() {
        assertEquals(BigInteger.valueOf(756), ConstrainedProducts.count());
        assertEquals(ConstrainedProducts.formulaCount(), ConstrainedProducts.count());
        assertEquals(756, allPizzas().size());
        assertEquals(756, new HashSet<>(allPizzas()).size());
    }

    @Test
    void everyPizzaObeysItsPoolsRules() {
        for (List<Object> pizza : allPizzas()) {
            assertTrue(ConstrainedProducts.BASES.contains(pizza.get(0)));
            assertTrue(ConstrainedProducts.CHEESES.contains(pizza.get(1)));
            assertTrue(ConstrainedProducts.CHEESES.contains(pizza.get(2)));
            assertFalse(pizza.get(1).equals(pizza.get(2)), "cheeses must differ: " + pizza);
            assertTrue(ConstrainedProducts.SAUCES.contains(pizza.get(3)));
            assertTrue(ConstrainedProducts.SAUCES.contains(pizza.get(4)));
            List<Object> toppings = pizza.subList(5, pizza.size());
            assertTrue(toppings.size() >= 1 && toppings.size() <= 3, "1 to 3 toppings: " + pizza);
            assertEquals(toppings.size(), new HashSet<>(toppings).size(), "toppings must differ: " + pizza);
            assertTrue(ConstrainedProducts.TOPPINGS.containsAll(toppings));
        }
    }

    @Test
    void sauceRepeatsAreAllowedAndCheeseRepeatsAreNot() {
        assertTrue(allPizzas().stream().anyMatch(p -> p.get(3).equals(p.get(4))));
        assertTrue(allPizzas().stream().noneMatch(p -> p.get(1).equals(p.get(2))));
    }

    @Test
    void jumpingToAPositionAgreesWithListingInOrder() {
        List<List<Object>> all = allPizzas();
        for (int position : new int[]{0, 1, 250, 500, 755}) {
            assertEquals(all.get(position), ConstrainedProducts.atPosition(position), "position " + position);
        }
    }

    @Test
    void everyNthTakesPositionsZeroNAndSoOn() {
        List<List<Object>> all = allPizzas();
        // 756 pizzas: positions 0, 250, 500 and 750 are below 756
        assertEquals(List.of(all.get(0), all.get(250), all.get(500), all.get(750)), ConstrainedProducts.everyNth(250));
    }

    @Test
    void aToppingRangeLargerThanItsPoolIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> JNumberTools.cartesianProduct()
                .constrainedProductOfDistinct(1, ConstrainedProducts.BASES)
                .andInRange(1, 5, ConstrainedProducts.TOPPINGS));
        assertFalse(ConstrainedProducts.rangeLargerThanPool().equals("accepted"));
    }

    @Test
    void describeReadsTheFlatList() {
        assertEquals("Small, cheese Ricotta + Mozzarella, sauce Tomato + Tomato, toppings tomato",
                ConstrainedProducts.describe(ConstrainedProducts.first(1).getFirst()));
    }
}
