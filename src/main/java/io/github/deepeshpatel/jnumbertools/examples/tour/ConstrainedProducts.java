/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.product.constrained.ConstrainedProductBuilder;

import java.math.BigInteger;
import java.util.List;

/**
 * A constrained Cartesian product: one choice from every pool, each pool with its own rule.
 * <p>
 * A simple product takes exactly one item from each list. A constrained product lets each pool
 * contribute a <em>selection</em> instead, so a pizza is built from four pools at once:
 * <ul>
 *   <li>{@code constrainedProductOfDistinct(1, bases)}: 1 base out of 3</li>
 *   <li>{@code andDistinct(2, cheeses)}: 2 different cheeses out of 3</li>
 *   <li>{@code andMultiSelect(2, sauces)}: 2 sauces out of 3, the same sauce allowed twice</li>
 *   <li>{@code andInRange(1, 3, toppings)}: between 1 and 3 different toppings out of 4</li>
 * </ul>
 * The library counts the pizzas, lists them in order, jumps to the one at a given position, or takes
 * every m-th one, without generating the others. Each pizza is a flat list: base, two cheeses, two
 * sauces, then the toppings. The range of {@code andInRange} may not exceed the size of its pool
 * (asking for 1 to 5 toppings out of 4 is rejected).
 */
public final class ConstrainedProducts {

    static final List<String> BASES = List.of("Small", "Medium", "Large");
    static final List<String> CHEESES = List.of("Ricotta", "Mozzarella", "Cheddar");
    static final List<String> SAUCES = List.of("Tomato", "White", "Green");
    static final List<String> TOPPINGS = List.of("tomato", "capsicum", "onion", "paneer");

    private ConstrainedProducts() {
    }

    private static ConstrainedProductBuilder pizzas() {
        return JNumberTools.cartesianProduct()
                .constrainedProductOfDistinct(1, BASES)
                .andDistinct(2, CHEESES)
                .andMultiSelect(2, SAUCES)
                .andInRange(1, 3, TOPPINGS);
    }

    /** @return number of different pizzas, from the library */
    public static BigInteger count() {
        return pizzas().count();
    }

    /** @return the same number from formulas: C(3,1) x C(3,2) x C(3+2-1,2) x (C(4,1) + C(4,2) + C(4,3)) */
    public static BigInteger formulaCount() {
        Calculator c = new Calculator();
        BigInteger toppings = c.nCr(4, 1).add(c.nCr(4, 2)).add(c.nCr(4, 3));
        return c.nCr(3, 1).multiply(c.nCr(3, 2)).multiply(c.nCrRepetitive(3, 2)).multiply(toppings);
    }

    /** @return the first {@code n} pizzas in lexicographic order */
    public static List<List<Object>> first(int n) {
        return pizzas().lexOrder().stream().limit(n).toList();
    }

    /** @return the pizza at the given position (counting from 0), reached directly with {@code byRanks} */
    public static List<Object> atPosition(long position) {
        return pizzas().byRanks(List.of(BigInteger.valueOf(position))).iterator().next();
    }

    /** @return every {@code step}-th pizza starting from position 0, taken with {@code lexOrderMth} */
    public static List<List<Object>> everyNth(int step) {
        return pizzas().lexOrderMth(step, 0).stream().toList();
    }

    /** @return the library's message for asking 1 to 5 toppings out of 4, or "accepted" */
    public static String rangeLargerThanPool() {
        try {
            JNumberTools.cartesianProduct()
                    .constrainedProductOfDistinct(1, BASES)
                    .andInRange(1, TOPPINGS.size() + 1, TOPPINGS);
            return "accepted";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    /** @return a readable line for one pizza, such as {@code Small, cheese Ricotta + Mozzarella, ...} */
    public static String describe(List<Object> pizza) {
        return pizza.get(0)
                + ", cheese " + pizza.get(1) + " + " + pizza.get(2)
                + ", sauce " + pizza.get(3) + " + " + pizza.get(4)
                + ", toppings " + pizza.subList(5, pizza.size()).stream().map(String::valueOf)
                .reduce((a, b) -> a + " + " + b).orElse("none");
    }

    public static void run() {
        System.out.println("Pizza: 1 base of " + BASES.size() + ", 2 different cheeses of " + CHEESES.size()
                + ", 2 sauces of " + SAUCES.size() + " (repeats allowed), 1 to 3 toppings of " + TOPPINGS.size());
        System.out.println("Pizzas: " + count() + " (formula: " + formulaCount() + ")");

        System.out.println();
        System.out.println("First three:");
        first(3).forEach(p -> System.out.println("  " + describe(p)));

        System.out.println("Pizza at position 500 (counting from 0), reached directly:");
        System.out.println("  " + describe(atPosition(500)));

        System.out.println("Every 250th pizza:");
        everyNth(250).forEach(p -> System.out.println("  " + describe(p)));

        System.out.println();
        System.out.println("1 to 5 toppings of 4 is refused: " + rangeLargerThanPool());
    }

    public static void main(String[] args) {
        run();
    }
}
