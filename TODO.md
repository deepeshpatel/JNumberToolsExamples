# TODO

Worklist for JNumberToolsExamples. Update this file in every patch. Mark `[x]` only when the work is merged and verified.

Target: JNumberTools **4.0.1** from Maven Central (fallback: 4.0.0 if 4.0.1 cannot be resolved).

## Chunk 1: Agent guidance
- [x] `.agents/skills/jnumbertools-examples/SKILL.md`
- [x] `TODO.md`

## Chunk 2: Project skeleton
- [x] `LICENSE` (Apache-2.0)
- [x] `pom.xml` (Apache-2.0 license block, Java 21, `jnumbertools.version` property, JUnit 5 test scope, exec plugin)
- [x] `.gitignore`
- [x] Old README replaced with a short skeleton (full README comes last)
- [x] Old dump removed (`src/`, `.idea/`); examples are rewritten against 4.0.1 from scratch
- [ ] Local check: `mvn -U clean verify` resolves jnumbertools 4.0.1 from Maven Central (not yet exercised; falls back to 4.0.0 if needed)

## Chunk 3: Bootstrap and dependency proof
- [x] `Example` record, `Examples` registry and `Main` runner (list / run ids / `all`)
- [x] `basics/QuickTour` (first example: one call per generator family)
- [x] ~~`DependencyVersionTest`~~ dropped; the CI build with `mvn -U` is the dependency check
- [x] `ExamplesSmokeTest` and `QuickTourTest`
- [x] CI: GitHub Actions workflow running `mvn -B -U clean verify` and all examples on Java 21
- [ ] Local check: JUnit tests actually executed with Maven (not yet run; sandbox has no Maven Central access)

## Chunk 4+: Examples (one chunk per 2 to 3 examples)
Products
- [x] Test-matrix generation: browser x OS x network with a compatibility predicate and `lexOrderMth` subset (`testing/CrossPlatformTestMatrix`)
- [x] A/B/n experiment design: product, stable per-visitor assignment by rank, reproducible `sample` (`experiments/AbTestDesign`)
Combinations
- [x] Lottery odds: `count`, rank and unrank of a ticket, `Calculator.nCr` (`lottery/LotteryOdds`)
- [x] Poker hands: enumerate 52 choose 5, reproduce the odds table, seeded `sample` (`games/PokerHands`)
- [x] Committee selection with a role mix (constrained product of combinations) (`hr/CommitteeSelection`)
Permutations
- [x] Delivery route brute force for a small number of stops (`unique`) (`logistics/DeliveryRoute`)
- [x] DNA k-mers and motif rearrangements (`multiset`, `repetitive`) (`bio/DnaMotifs`)
- [x] Keyspace partitioning for N workers with `lexOrderMth` and `byRanks` (`security/KeyspacePartitioning`)
Derangements
- [x] Secret Santa: random derangement via `sample`, count via `subFactorial` (`groups/SecretSanta`)
Subsets
- [x] Feature-flag configurations: all subsets, size ranges (`config/FeatureFlagTesting`)
Ranking and number systems
- [x] Compact permutation storage: `rankOf` / `unrankOf` of a shuffled deck (`storage/CompactPermutationStorage`)

## Last chunk: Documentation
- [x] README: overview table, real outputs, how to run, how the dependency is verified
- [x] CONTRIBUTING.md (short; points to the skill file)
- [x] `src/main/javadoc/overview.html` (needed by the release profile's javadoc step)
- [x] `ReadmeTest` keeps README and registry in step
- [ ] Final check: clean clone, `mvn -U clean verify`, `mvn -P release` dry run, all examples run

## Review follow-ups
- [x] `Main`: friendly error and exit status 1 for an unknown id, `all` anywhere runs everything, `MainTest`; docs use `mvn -q compile exec:java` (exec:java alone does not compile)
- [x] `CommitteeSelection`: shortlist respects the conflict rule
- [x] `AbTestDesign`: SHA-256 with an experiment name instead of `String.hashCode`
- [x] Docs: remove references to the dropped `DependencyVersionTest`, lottery "rank" wording, fair storage comparison, dead surefire property
- [x] Tests: keep exactly one seed-pinned canary (poker deal), everything else property based
- [ ] First release: set `1.0.0` (no SNAPSHOT) before `mvn clean deploy -Prelease`

## Tour and use cases ported from the old in-library examples
Tour (API areas)
- [x] `tour/NumberSystems`: factoradic, combinadic, derangadic, every printed claim recomputed
- [x] `tour/Sampling`: `choice` (with replacement) vs `sample` (distinct)
- [x] `tour/Selections`: ordered or not, with or without repetition, plus multiset
- [x] `tour/ConstrainedProducts`: the pizza example, counted, listed and jumped into
Use cases
- [x] `usecases/scheduling/RoundRobinTournament`: a valid round-robin schedule, checked against the library's pairings
- [x] `usecases/statistics/DiceSimulation`: exact distribution by enumeration vs a seeded simulation
Not ported yet
- [ ] Stratified survey sampling, PowerBall (combination x single pick), pairwise coverage of an every-Mth subset, stream integration
Dropped on purpose: the templated "product of invented lists" examples, the timing benchmark, and the old "pairwise" example (it was not pairwise).

## Optional
- [ ] Encode a subset as one integer with `combinadic` (`numberSystem().combinadic(..)`)

## Decisions
- Examples consume only the public builder API plus `Calculator`.
- Cross-dimension rules are shown as stream filters, not as a library feature.
- Old README use cases that relied on removed APIs (`withConstraint`, `lexOrderNth`) and the Hadoop MapReduce sample are dropped; any retained idea is rewritten against 4.0.1.
