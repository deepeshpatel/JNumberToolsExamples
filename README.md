# JNumberTools Examples

[![build](https://github.com/deepeshpatel/JNumberToolsExamples/actions/workflows/build.yml/badge.svg)](https://github.com/deepeshpatel/JNumberToolsExamples/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)

Real-life, runnable examples of [JNumberTools](https://github.com/deepeshpatel/JNumberTools), an open-source Java library for combinatorics and number systems.

It showcases the library on realistic problems: test matrices, work partitioning, lottery odds, Secret Santa, compact storage of orderings and more. It uses the published `io.github.deepeshpatel:jnumbertools:4.0.1` artifact from Maven Central as a plain dependency, nothing else, and CI builds with `mvn -U` to force a fresh resolve.

## Dependency

```xml
<dependency>
    <groupId>io.github.deepeshpatel</groupId>
    <artifactId>jnumbertools</artifactId>
    <version>4.0.1</version>
</dependency>
```

The version is declared once, in the `jnumbertools.version` property of [pom.xml](pom.xml).

## Requirements

- Java 21 or newer
- Maven 3.9 or newer

## Run

```
mvn -U clean verify                                  # build and run the tests
mvn -q compile exec:java                             # list the examples
mvn -q compile exec:java -Dexec.args="poker-hands"   # run one example
mvn -q compile exec:java -Dexec.args="all"           # run every example
```

Every example is also a plain class with a `main` method, so it runs from any IDE.

## Examples

| Id | Example | Library calls |
|---|---|---|
| `quick-tour` | [Quick tour](src/main/java/io/github/deepeshpatel/jnumbertools/examples/basics/QuickTour.java) | `permutations().unique`, `combinations().unique`, `subsets().of(..).all()`, `derangements().of`, `cartesianProduct().simpleProductOf` |
| `test-matrix` | [Cross-platform test matrix](src/main/java/io/github/deepeshpatel/jnumbertools/examples/testing/CrossPlatformTestMatrix.java) | `cartesianProduct().simpleProductOf(..).and(..)`, `lexOrder`, `lexOrderMth`, `count` |
| `keyspace-partitioning` | [Keyspace partitioning for N workers](src/main/java/io/github/deepeshpatel/jnumbertools/examples/security/KeyspacePartitioning.java) | `permutations().repetitive`, `lexOrderMth`, `byRanks`, `count` |
| `secret-santa` | [Secret Santa](src/main/java/io/github/deepeshpatel/jnumbertools/examples/groups/SecretSanta.java) | `derangements().of`, `sample`, `count`, `Calculator.subFactorial` |
| `lottery-odds` | [Lottery odds and ticket numbers](src/main/java/io/github/deepeshpatel/jnumbertools/examples/lottery/LotteryOdds.java) | `combinations().unique`, `rankOf().combination()`, `unrankOf().combination()`, `Calculator.nCr` |
| `deck-storage` | [Compact permutation storage](src/main/java/io/github/deepeshpatel/jnumbertools/examples/storage/CompactPermutationStorage.java) | `rankOf().permutation().unique`, `unrankOf().permutation().unique` |
| `dna-motifs` | [DNA words and motif rearrangements](src/main/java/io/github/deepeshpatel/jnumbertools/examples/bio/DnaMotifs.java) | `permutations().repetitive`, `permutations().multiset`, `Calculator.multinomial` |
| `delivery-route` | [Shortest delivery round trip](src/main/java/io/github/deepeshpatel/jnumbertools/examples/logistics/DeliveryRoute.java) | `permutations().unique`, `lexOrder`, `count` |
| `feature-flags` | [Feature-flag configurations](src/main/java/io/github/deepeshpatel/jnumbertools/examples/config/FeatureFlagTesting.java) | `subsets().of(..).all()`, `subsets().of(..).inRange(from, to)`, `count` |
| `poker-hands` | [Poker hand odds](src/main/java/io/github/deepeshpatel/jnumbertools/examples/games/PokerHands.java) | `combinations().unique(52, 5)`, `lexOrder`, `sample`, `count` |
| `committee-selection` | [Committee selection](src/main/java/io/github/deepeshpatel/jnumbertools/examples/hr/CommitteeSelection.java) | `cartesianProduct().constrainedProductOfDistinct(..).andDistinct(..)`, `lexOrder`, `sample`, `count` |
| `ab-test-design` | [A/B/n experiment design](src/main/java/io/github/deepeshpatel/jnumbertools/examples/experiments/AbTestDesign.java) | `cartesianProduct().simpleProductOf`, `byRanks`, `sample`, `count` |

All outputs below were produced by running the examples. Random choices use a fixed seed, so the output is reproducible.

A note on rules between dimensions: JNumberTools builds the products, subsets and orderings. Rules such as "Safari only runs on macOS" or "gift wrap needs the new checkout" are not a library feature, so the examples apply them as plain stream filters and say so.

### Quick tour

`quick-tour` - One call per generator family. Counts that no loop could ever reach are answered instantly.

Source: [`basics/QuickTour.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/basics/QuickTour.java)

```
Permutations of 4 items          : 24
First 3 permutations of A,B,C    : [[A, B, C], [A, C, B], [B, A, C]]
Permutations of 20 items         : 2432902008176640000
5 choose 2                       : 10
Poker hands (52 choose 5)        : 2598960
Subsets of 3 items               : 8
Derangements of 4 items          : 9
Sizes x colours                  : 6
```

### Cross-platform test matrix

`test-matrix` - Browser x OS x network, with compatibility rules applied as a stream filter and a deterministic smoke subset taken with `lexOrderMth`.

Source: [`testing/CrossPlatformTestMatrix.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/testing/CrossPlatformTestMatrix.java)

```
Full product (browser x system x network): 40
Supported combinations                   : 28

Smoke subset (every 4th rank, supported only):
  [Chrome, Windows, fast]
  [Chrome, Linux, fast]
  [Chrome, iOS, fast]
  [Firefox, macOS, fast]
  [Firefox, Android, fast]
  [Safari, iOS, fast]
  [Edge, macOS, fast]
```

### Keyspace partitioning for N workers

`keyspace-partitioning` - Split a space of 218 trillion candidates between workers without enumerating it: interleaved ranks for small spaces, contiguous rank ranges for huge ones.

Source: [`security/KeyspacePartitioning.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/security/KeyspacePartitioning.java)

```
4-digit PIN space: 10000 candidates, 8 workers, interleaved
  worker 0: 1250 candidates, first three [0000, 0008, 0016]
  worker 1: 1250 candidates, first three [0001, 0009, 0017]
  worker 2: 1250 candidates, first three [0002, 0010, 0018]
  worker 3: 1250 candidates, first three [0003, 0011, 0019]
  worker 4: 1250 candidates, first three [0004, 0012, 0020]
  worker 5: 1250 candidates, first three [0005, 0013, 0021]
  worker 6: 1250 candidates, first three [0006, 0014, 0022]
  worker 7: 1250 candidates, first three [0007, 0015, 0023]

8-character alphanumeric space: 218340105584896 candidates, 4 workers, contiguous
  worker 0: ranks [0, 54585026396224) aaaaaaaa .. pE999999
  worker 1: ranks [54585026396224, 109170052792448) pFaaaaaa .. E9999999
  worker 2: ranks [109170052792448, 163755079188672) Faaaaaaa .. UE999999
  worker 3: ranks [163755079188672, 218340105584896) UFaaaaaa .. 99999999

At an assumed 1 billion guesses/second per worker, 4 workers need about 15 hours for the whole space.
```

### Secret Santa

`secret-santa` - A gift draw where nobody gets themselves, sampled directly from the valid arrangements instead of shuffle-and-retry.

Source: [`groups/SecretSanta.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/groups/SecretSanta.java)

```
People: 6, valid draws: 265 of 720 possible shuffles
A plain shuffle would be valid only 36.8% of the time

Draw (seed 42):
  Aarav -> Dmitri
  Bella -> Elena
  Chen -> Farid
  Dmitri -> Aarav
  Elena -> Chen
  Farid -> Bella
```

### Lottery odds and ticket numbers

`lottery-odds` - 6/49 odds by prize tier, and a ticket turned into its position among all tickets and back.

Source: [`lottery/LotteryOdds.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/lottery/LotteryOdds.java)

```
Tickets in a 6/49 lottery: 13983816
Jackpot odds: 1 in 13983816

Exactly 6 matches: 0.000007%
Exactly 5 matches: 0.001845%
Exactly 4 matches: 0.096862%
Exactly 3 matches: 1.765040%

Ticket [7, 14, 21, 28, 35, 42] has rank 8391494 (counting from 0) among 13983816 tickets
The ticket with rank 10000000 is [9, 19, 32, 34, 35, 46]
Quick pick (seed 42): [10, 12, 14, 17, 20, 34]
```

### Compact permutation storage

`deck-storage` - A shuffled 52-card deck stored as one integer of at most 226 bits and restored exactly.

Source: [`storage/CompactPermutationStorage.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/storage/CompactPermutationStorage.java)

```
Shuffled deck (seed 42), first 8 cards: 3H AC 9D TD 7D 2S 9H 5S

Orderings of 52 cards     : 80658175170943878571660636856403766975289505440883277824000000000000
Rank of this deck         : 24443007826809818884276771602767457113196962187470522132311676778611
Rank in base 36           : 2xjq30tm7kh915newezu1xbgz5zaqq5utnuu9yftee7n
Bits needed               : 224 (one byte per card uses 416, six bits per card 312)
Restored deck is identical: true
```

### DNA words and motif rearrangements

`dna-motifs` - All k-mers over ACGT, and every distinct rearrangement of a motif with no duplicates from repeated bases.

Source: [`bio/DnaMotifs.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/bio/DnaMotifs.java)

```
DNA words of length 4 : 256, first five [AAAA, AAAC, AAAG, AAAT, AACA]
DNA words of length 12: 16777216

Motif GATTACA has {G=1, A=3, T=2, C=1}
Orderings of 7 positions       : 5040
Distinct rearrangements        : 420
Multinomial 7!/(3! 2! 1! 1!)   : 420
First five rearrangements      : [GAAATTC, GAAATCT, GAAACTT, GAATATC, GAATACT]
```

### Shortest delivery round trip

`delivery-route` - Brute force over every visiting order, with the counts that show when brute force stops being sensible.

Source: [`logistics/DeliveryRoute.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/logistics/DeliveryRoute.java)

```
4 stops: 24 possible orders
8 stops: 40320 possible orders
10 stops: 3628800 possible orders
12 stops: 479001600 possible orders

Alphabetical route [A, B, C, D, E, F, G, H]: 46.30 km
Shortest route     [A, F, D, G, C, E, B, H]: 29.95 km
```

### Feature-flag configurations

`feature-flags` - Test every configuration with at most two flags on, sized by range, with a dependency rule as a filter.

Source: [`config/FeatureFlagTesting.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/config/FeatureFlagTesting.java)

```
Flags: [newCheckout, darkMode, betaSearch, fastShipping, giftWrap]
All configurations          : 32
With at most 2 flags on     : 16
With 1 or 2 flags on        : 15
20 flags, all configurations: 1048576

Configurations to test (at most 2 on, giftWrap needs newCheckout):
  []
  [newCheckout]
  [darkMode]
  [betaSearch]
  [fastShipping]
  [newCheckout, darkMode]
  [newCheckout, betaSearch]
  [newCheckout, fastShipping]
  [newCheckout, giftWrap]
  [darkMode, betaSearch]
  [darkMode, fastShipping]
  [betaSearch, fastShipping]
```

### Poker hand odds

`poker-hands` - Enumerate all 2,598,960 hands and reproduce the textbook odds table exactly.

Source: [`games/PokerHands.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/games/PokerHands.java)

```
Five-card hands: 2598960

  STRAIGHT_FLUSH          40    0.0015%  1 in 64974.0
  FOUR_OF_A_KIND         624    0.0240%  1 in 4165.0
  FULL_HOUSE            3744    0.1441%  1 in 694.2
  FLUSH                 5108    0.1965%  1 in 508.8
  STRAIGHT             10200    0.3925%  1 in 254.8
  THREE_OF_A_KIND      54912    2.1128%  1 in 47.3
  TWO_PAIR            123552    4.7539%  1 in 21.0
  ONE_PAIR           1098240   42.2569%  1 in 2.4
  HIGH_CARD          1302540   50.1177%  1 in 2.0

Deal (seed 42): [3S, JH, 2C, 3C, JC] -> TWO_PAIR
```

### Committee selection

`committee-selection` - A committee with a fixed role mix from three pools, counted, listed and sampled, with a conflict-of-interest filter.

Source: [`hr/CommitteeSelection.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/hr/CommitteeSelection.java)

```
Candidates: 5 engineers, 3 designers, 4 managers
Committees (2 engineers + 1 designer + 1 manager): 120
Without the E1 + M1 conflict                     : 108

First three valid committees:
  [E1, E2, D1, M2]
  [E1, E2, D1, M3]
  [E1, E2, D1, M4]
Shortlist of three random committees (seed 42):
  [E2, E3, D1, M3]
  [E3, E5, D1, M2]
  [E2, E5, D2, M1]
```

### A/B/n experiment design

`ab-test-design` - 18 landing-page variants: a stable variant per visitor by hashing to a rank, and a seeded pilot set.

Source: [`experiments/AbTestDesign.java`](src/main/java/io/github/deepeshpatel/jnumbertools/examples/experiments/AbTestDesign.java)

```
Variants: 18 (3 headlines x 3 buttons x 2 layouts)

Visitor user-1 always sees [Save time, green, single-column]
Visitor user-2 always sees [Save time, green, two-column]
Visitor alice@example.com always sees [Save money, orange, two-column]
Same visitor, another experiment (checkout-2026): user-1 sees [Save time, blue, two-column]

1800 visitors over 18 variants: fewest 84, most 118 (ideal 100 each; some random spread is normal)

Pilot with six distinct variants (seed 42):
  [Save money, blue, single-column]
  [Join today, green, single-column]
  [Save time, green, two-column]
  [Join today, orange, two-column]
  [Join today, orange, single-column]
  [Save money, blue, two-column]
```

## Project layout

```
src/main/java/.../examples/
    Example.java, Examples.java, Main.java   runner and registry
    <domain>/<Example>.java                  one class per example
src/test/java/...                            dependency proof, smoke tests, one test class per example
.agents/skills/jnumbertools-examples/        guidance for AI assistants
TODO.md                                      worklist
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

[Apache License 2.0](LICENSE)
