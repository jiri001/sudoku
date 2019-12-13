# Sudoku

This is a Sudoku solver implemented in Java. It contains two main search algorithms:

  - Full Search
  - Genetic Search

### Description

**Full search** uses known back-tracking algorithm and is very fast. Sudokus with easy ranking is solves in 1ms and Sudokus with expert ranking in little more :)

**Genetic search** is experimental now, but it was proved it solves both easy and expert Sudokus (https://sudoku.com/expert/). Easy one in a couple of seconds and expert ones in several minutes, depending on your luck :) Genetic implementation is done by myself, selection is tournament-based. It allows to set following parameters:
* MUTATION_RATIO
* CROSSOVER_RATIO
* TOURNAMENT_SIZE
* mPopulationSize
* mElitismRatio

Genetic search needs more experiments and fine tuning, I believe it could work much better, but I did not have more time now. In order to avoid local optimum, I implemented a restart of the computation (and new random population generation) in case some threshold of number of stagnation iterations is exceeded (meaning where fitness stagnates). Fitness here is understood as negative function, measuring total error of the current state of the matrix (0 is the best fitness and more negative means worse fitness). Hence tournament selection method was chosen.

### Run

* Pre-requisites: Java 1.8 runtime environment, for Genetic Search 64b version is needed.
```
\sudo\bin>run.bat
\sudo\bin>java -d64 -classpath ".;.\libs\*" cz.zettel.algorithms.Example
```

### Example
```
############ START REPORT FullSearch ############
************ ORIGINAL MATRIX ************
[0, 1, 3, 8, 0, 0, 4, 0, 5]
[0, 2, 4, 6, 0, 5, 0, 0, 0]
[0, 8, 7, 0, 0, 0, 9, 3, 0]
[4, 9, 0, 3, 0, 6, 0, 0, 0]
[0, 0, 1, 0, 0, 0, 5, 0, 0]
[0, 0, 0, 7, 0, 1, 0, 9, 3]
[0, 6, 9, 0, 0, 0, 7, 4, 0]
[0, 0, 0, 2, 0, 7, 6, 8, 0]
[1, 0, 2, 0, 0, 8, 3, 5, 0]
************ CALCULATED MATRIX ************
[6, 1, 3, 8, 7, 9, 4, 2, 5]
[9, 2, 4, 6, 3, 5, 1, 7, 8]
[5, 8, 7, 1, 2, 4, 9, 3, 6]
[4, 9, 8, 3, 5, 6, 2, 1, 7]
[7, 3, 1, 9, 8, 2, 5, 6, 4]
[2, 5, 6, 7, 4, 1, 8, 9, 3]
[8, 6, 9, 5, 1, 3, 7, 4, 2]
[3, 4, 5, 2, 9, 7, 6, 8, 1]
[1, 7, 2, 4, 6, 8, 3, 5, 9]
Elapsed time: 1ms
############ END REPORT FullSearch ############


############ START REPORT FullSearch ############
************ ORIGINAL MATRIX ************
[0, 0, 2, 0, 0, 0, 0, 4, 1]
[0, 0, 0, 0, 8, 2, 0, 7, 0]
[0, 0, 0, 0, 4, 0, 0, 0, 9]
[2, 0, 0, 0, 7, 9, 3, 0, 0]
[0, 1, 0, 0, 0, 0, 0, 8, 0]
[0, 0, 6, 8, 1, 0, 0, 0, 4]
[1, 0, 0, 0, 9, 0, 0, 0, 0]
[0, 6, 0, 4, 3, 0, 0, 0, 0]
[8, 5, 0, 0, 0, 0, 4, 0, 0]
************ CALCULATED MATRIX ************
[6, 3, 2, 9, 5, 7, 8, 4, 1]
[4, 9, 1, 6, 8, 2, 5, 7, 3]
[7, 8, 5, 3, 4, 1, 2, 6, 9]
[2, 4, 8, 5, 7, 9, 3, 1, 6]
[3, 1, 9, 2, 6, 4, 7, 8, 5]
[5, 7, 6, 8, 1, 3, 9, 2, 4]
[1, 2, 4, 7, 9, 5, 6, 3, 8]
[9, 6, 7, 4, 3, 8, 1, 5, 2]
[8, 5, 3, 1, 2, 6, 4, 9, 7]
Elapsed time: 22ms
############ END REPORT FullSearch ############

2019-12-08 16:26:32,025 3858 [main] INFO  c.z.algorithms.logic.GeneticSearch - Matrix found !

############ START REPORT GeneticSearch ############
************ ORIGINAL MATRIX ************
[0, 1, 3, 8, 0, 0, 4, 0, 5]
[0, 2, 4, 6, 0, 5, 0, 0, 0]
[0, 8, 7, 0, 0, 0, 9, 3, 0]
[4, 9, 0, 3, 0, 6, 0, 0, 0]
[0, 0, 1, 0, 0, 0, 5, 0, 0]
[0, 0, 0, 7, 0, 1, 0, 9, 3]
[0, 6, 9, 0, 0, 0, 7, 4, 0]
[0, 0, 0, 2, 0, 7, 6, 8, 0]
[1, 0, 2, 0, 0, 8, 3, 5, 0]
************ CALCULATED MATRIX ************
[6, 1, 3, 8, 7, 9, 4, 2, 5]
[9, 2, 4, 6, 3, 5, 1, 7, 8]
[5, 8, 7, 1, 2, 4, 9, 3, 6]
[4, 9, 8, 3, 5, 6, 2, 1, 7]
[7, 3, 1, 9, 8, 2, 5, 6, 4]
[2, 5, 6, 7, 4, 1, 8, 9, 3]
[8, 6, 9, 5, 1, 3, 7, 4, 2]
[3, 4, 5, 2, 9, 7, 6, 8, 1]
[1, 7, 2, 4, 6, 8, 3, 5, 9]
Elapsed time: 3539ms
############ END REPORT GeneticSearch ############

2019-12-08 16:28:05,084 96917 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:29:42,426 194259 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:31:21,979 293812 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:32:58,579 390412 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:34:32,309 484142 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 3
2019-12-08 16:36:06,340 578173 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:37:40,910 672743 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:39:12,738 764571 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:40:49,530 861363 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:42:27,224 959057 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:44:00,669 1052502 [main] DEBUG c.z.algorithms.logic.GeneticSearch - Search restarted, maximum stagnation iterations (50) achieved. mCurrentMinimalError = 2
2019-12-08 16:44:54,696 1106529 [main] INFO  c.z.algorithms.logic.GeneticSearch - Matrix found !

############ START REPORT GeneticSearch ############
************ ORIGINAL MATRIX ************
[0, 0, 2, 0, 0, 0, 0, 4, 1]
[0, 0, 0, 0, 8, 2, 0, 7, 0]
[0, 0, 0, 0, 4, 0, 0, 0, 9]
[2, 0, 0, 0, 7, 9, 3, 0, 0]
[0, 1, 0, 0, 0, 0, 0, 8, 0]
[0, 0, 6, 8, 1, 0, 0, 0, 4]
[1, 0, 0, 0, 9, 0, 0, 0, 0]
[0, 6, 0, 4, 3, 0, 0, 0, 0]
[8, 5, 0, 0, 0, 0, 4, 0, 0]
************ CALCULATED MATRIX ************
[6, 3, 2, 9, 5, 7, 8, 4, 1]
[4, 9, 1, 6, 8, 2, 5, 7, 3]
[7, 8, 5, 3, 4, 1, 2, 6, 9]
[2, 4, 8, 5, 7, 9, 3, 1, 6]
[3, 1, 9, 2, 6, 4, 7, 8, 5]
[5, 7, 6, 8, 1, 3, 9, 2, 4]
[1, 2, 4, 7, 9, 5, 6, 3, 8]
[9, 6, 7, 4, 3, 8, 1, 5, 2]
[8, 5, 3, 1, 2, 6, 4, 9, 7]
Elapsed time: 1102629ms
############ END REPORT GeneticSearch ############

```

License
----

MIT
