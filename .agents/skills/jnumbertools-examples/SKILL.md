---
name: jnumbertools-examples
description: Project-specific guidance for AI assistants adding, reviewing, or maintaining examples in JNumberToolsExamples (real-life showcases of JNumberTools 4.0.x). Use whenever working in this repository.
---

# JNumberToolsExamples Skill

## 1. Purpose of this repository

Showcase JNumberTools (the published artifact `io.github.deepeshpatel:jnumbertools:4.0.1`) on real-life scenarios, each small, runnable and readable.

This repository is a *consumer* of the library. Never copy library source here, never depend on library internals, never use `SNAPSHOT` or `systemPath` dependencies.

## 2. Ground rules

- Read `TODO.md` first and update it in the same change that does the work. It is the single worklist.
- The library version lives in exactly one place: the `jnumbertools.version` property in `pom.xml`.
- Java 21 (`maven.compiler.release` 21), same minimum as the library. No other runtime dependencies besides JNumberTools. JUnit 5 is test-scope only.
- Every `.java` file starts with `/* Copyright (c) <year> Deepesh Patel / SPDX-License-Identifier: Apache-2.0 */` (same short header as the library); `pom.xml` carries the same line as an XML comment and declares the Apache-2.0 license; `LICENSE` is the unmodified Apache-2.0 text. No LGPL text anywhere.
- Use only the **public builder API** (`JNumberTools.permutations()`, `combinations()`, `subsets()`, `derangements()`, `cartesianProduct()`, `rankOf()`, `unrankOf()`, `numberSystem()`) plus `Calculator`, as the library's own skill requires. Never construct generator classes directly.
- Package root: `io.github.deepeshpatel.jnumbertools.examples`. The library's package root is `io.github.deepeshpatel.jnumbertools` (not `com.github...`).
- Never write example output by hand. Run the code and paste the real output. Random examples use a fixed seed (`new Random(42)`) so output is reproducible.

## 3. Verified API cheat sheet (4.0.1)

Every builder offers: `lexOrder()`, `lexOrderMth(m, start)`, `byRanks(Iterable<BigInteger>)`, `choice(n, Random)` (with replacement), `sample(n, Random)` (distinct), `count()` (`BigInteger`), `isEmpty()`. Results are `StreamableIterable<List<T>>`; call `.stream()` or iterate.

| Area | Entry points |
|---|---|
| Permutations | `permutations().unique(n)` / `.unique(list)`, `.nPk(k, list)`, `.repetitive(r, list)`, `.multiset(LinkedHashMap<T,Integer>)` |
| Combinations | `combinations().unique(r, list)`, `.unique(n, r)`, `.repetitive(r, list)`, `.multiset(LinkedHashMap<T,Integer>, r)` |
| Subsets | `subsets().of(list)` / `.of(n)`, then **`.all()` or `.inRange(from, to)` is mandatory** (without it `count()` is 0 and `lexOrder()` throws) |
| Derangements | `derangements().of(list)`, `.of(n)` |
| Products | `cartesianProduct().simpleProductOf(list).and(list)...`; `.constrainedProductOfDistinct(q, list).andDistinct(..).andMultiSelect(q, list).andInRange(from, to, list)` |
| Ranking | `rankOf().permutation()...`, `unrankOf().permutation()...` |
| Number systems | `numberSystem().factoradic(..)`, `.combinadic(value, degree)`, `.permutadic(value, degree)`, `.derangadic(rank, order)` |
| Math | `new Calculator()`: `nCr`, `nPr`, `factorial`, `subFactorial`, `power`, `multinomial`, `rencontresNumber`, `telephoneNumber` |

Argument-order trap: `combinations().unique(int n, int r)` takes `n` first, `combinations().unique(int r, List)` takes `r` first. A single boxed `Integer`/`Character` argument can resolve to the `int` overload; pass a `List` when in doubt.

Names that **do not exist** in 4.0.x (they appear in the old README): `lexOrderNth`, `withConstraint`, the `com.github.deepeshpatel` package, and `constrainedProductOf` (deprecated for removal; use `constrainedProductOfDistinct`). The library has no Hadoop/Spark/Flink connectors; do not claim any.

Rules between dimensions (for example "Safari only on macOS") are not a library feature: filter the stream with a plain predicate and say so in the example.

## 4. Anatomy of an example

- One class per scenario under `src/main/java/.../examples/<domain>/`, with a `public static void main`, a class Javadoc stating the real-life problem in two or three sentences, and a package-private/static method that *returns* the result so tests can call it.
- Small enough to read in one screen. Prefer streams over manual loops. No unexplained magic numbers.
- Each example is registered in `Examples.java` (name, domain, one-line summary, runnable) so `mvn -q exec:java` can list and run them.
- Each example has a matching entry in the README table with the real output block.

## 5. Tests

- `src/test/java` is required. The tests keep the examples honest: they catch API drift when `jnumbertools.version` is bumped, wrong numbers in an example's own logic, and a README that no longer matches the registry.
- Each example has a test class asserting deterministic facts and properties (counts, first/last element, a known rank, round trips, no duplicates, gapless coverage). Do not snapshot entire outputs.
- Do not pin seeded `sample`/`choice` output: that tests the library's algorithm, not the example. The one deliberate exception is the canary `PokerHandsTest.dealIsReproducible`; if a library upgrade changes it, update it and the README together.
- Tests call the example's result method, not `main`.

## 6. Verification

- `mvn -U clean verify` must pass from a clean checkout with no local `~/.m2` entry for jnumbertools.
- If an assistant's sandbox cannot reach Maven Central, say so plainly; compile and run against the library source at the matching release commit instead, and state that the Central artifact itself was not exercised.
- Do not claim a build, test run, or output was observed unless it was actually run.

## 7. Working style

- Deliver work as small patches (one chunk of `TODO.md` per patch, `git format-patch` style), never one giant patch.
- Targeted edits over rewrites once a file exists.
- When a library behaviour looks wrong, write a minimal reproducer through the public builder and report it as a library issue; do not work around it silently in an example.
