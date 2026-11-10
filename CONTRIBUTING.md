# Contributing

Thanks for helping. This repository only consumes the published JNumberTools artifact; library issues belong in [JNumberTools](https://github.com/deepeshpatel/JNumberTools/issues).

## Adding an example

1. Pick a real-life problem that one or two JNumberTools calls solve well. Check [TODO.md](TODO.md) for open ideas.
2. Add one class under `src/main/java/io/github/deepeshpatel/jnumbertools/examples/<domain>/`:
   - a class Javadoc stating the real-life problem in a few sentences,
   - methods that **return** results (so tests can check them), a `run()` that prints, and a `main`,
   - only the public API: `JNumberTools.permutations()`, `combinations()`, `subsets()`, `derangements()`, `cartesianProduct()`, `rankOf()`, `unrankOf()`, `numberSystem()` and `Calculator`.
3. Register it in `Examples.java`.
4. Add a test class with deterministic assertions (counts, a known first element, a round trip). Use a fixed seed for anything random.
5. Add the example to the table and output section of `README.md`, using output you actually ran.
6. Tick or add the item in `TODO.md`.
7. Run `mvn -U clean verify`.

Rules between dimensions are not a library feature: apply them as stream filters and say so in the Javadoc.

## Style

- Java 21, no extra runtime dependencies. JUnit 5 for tests only.
- Every `.java` file starts with the header
  `/* Copyright (c) <year> Deepesh Patel / SPDX-License-Identifier: Apache-2.0 */`.
- Small patches: one example, or one theme, per pull request.

More detail for AI assistants is in [.agents/skills/jnumbertools-examples/SKILL.md](.agents/skills/jnumbertools-examples/SKILL.md).

## License

By contributing you agree that your contribution is licensed under the [Apache License 2.0](LICENSE).
