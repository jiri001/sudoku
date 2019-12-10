package cz.zettel.algorithms.logic;

import com.sun.istack.internal.NotNull;
import cz.zettel.algorithms.entity.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Genetic Algorithm class
 */
public class GeneticSearch extends AbstractSearch {
    private static final Logger log = LoggerFactory.getLogger(GeneticSearch.class.getName());

    /**
     * Percentage of population to be mutated.
     */
    private static final float MUTATION_RATIO = 0.1F;
    /**
     * Probability that the crossover happens.
     */
    private static final float CROSSOVER_RATIO = 1F;
    /**
     * K-individuals in each tournaments.
     */
    private static final int TOURNAMENT_SIZE = 2;
    /**
     * Restart the algorithm and create new random population if number of iteration the best fitness stagnates.
     */
    private static int STAGNATION_ITERATION_THRESHOLD = 50;

    /**
     * Size of the population.
     */
    private int mPopulationSize = 10000;
    /**
     * Percentage of population which are selected unchanged for new generation.
     */
    private float mElitismRatio = 0.2F;

    private Random mRandom;
    /**
     * Variables for calculating the stagnation.
     */
    private int mStagnationIteration, mCurrentMinimalError, mLastMinimalError;

    /**
     * Default constructor.
     *
     * @param initialMatrix
     */
    public GeneticSearch(@NotNull final Matrix initialMatrix) {
        super(initialMatrix);
        calculateMatrix();
        stopMeasuringTime();
    }

    /**
     * Constructor.
     *
     * @param initialMatrix
     * @param populationSize Size of initial population
     * @param elitismRatio   Elitism Ratio
     */
    public GeneticSearch(@NotNull final Matrix initialMatrix,
                         @NotNull final int populationSize,
                         @NotNull final float elitismRatio) {
        super(initialMatrix);
        mPopulationSize = populationSize;
        mElitismRatio = elitismRatio;
        calculateMatrix();
        stopMeasuringTime();
    }


    @Override
    void calculateMatrix() {
        List<Map.Entry<Matrix, Integer>> population, populationOffspring;
        mRandom = new Random();
        Matrix parent1, parent2;

        // create initial population
        population = createNewPopulation();

        do {
            log.trace("Stagnation iteration {}, mCurrentMinimalError {}, MUTATION_RATIO {}", mStagnationIteration, mCurrentMinimalError, MUTATION_RATIO);
            if (Thread.currentThread().isInterrupted()) {
                log.info("Interrupted.");
                break;
            }

            calculateFitness(population);

            populationOffspring = new ArrayList<>();

            // select elite individuals to offspring
            for (int i = 0; i < (int) (population.size() * mElitismRatio); i++) {
                populationOffspring.add(population.get(i));
            }

            // add remaining individuals to offspring by crossover
            while (populationOffspring.size() < mPopulationSize) {
                parent1 = tournamentSelection(population);
                parent2 = tournamentSelection(population);
                crossover(parent1, parent2, populationOffspring);
            }

            population = populationOffspring;

            // mutation for overall
            mutate(population);

            // if fitness stagnates for long => restart
            if (mStagnationIteration > STAGNATION_ITERATION_THRESHOLD) {
                mStagnationIteration = 0;
                mRandom = new Random();
                population = createNewPopulation();
                log.debug("Search restarted, maximum stagnation iterations ({}) achieved. mCurrentMinimalError = {}", STAGNATION_ITERATION_THRESHOLD, mCurrentMinimalError);
            }
        } while (mCurrentMinimalError > 0);

        log.info("Matrix found !");
        mSolvedMatrix = population.get(0).getKey();
    }

    /**
     * Creates initial population
     *
     * @return List of entries for each individual
     */
    private List<Map.Entry<Matrix, Integer>> createNewPopulation() {
        final List<Map.Entry<Matrix, Integer>> population;
        population = new ArrayList<>();
        for (int i = 0; i < mPopulationSize; i++) {
            population.add(new AbstractMap.SimpleEntry<Matrix, Integer>(createRandomAllowedMatrix(), -1));
        }
        return population;
    }

    /**
     * Select the best individual from the tournament group, which size is configurable.
     *
     * @param population
     * @return best Matrix
     */
    private Matrix tournamentSelection(final List<Map.Entry<Matrix, Integer>> population) {
        Map.Entry<Matrix, Integer> bestMatrix = null;
        Map.Entry<Matrix, Integer> randomMatrix;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            randomMatrix = population.get(mRandom.nextInt(population.size()));
            if (bestMatrix == null || randomMatrix.getValue() < bestMatrix.getValue()) {
                bestMatrix = randomMatrix;
            }
        }
        return bestMatrix.getKey();
    }

    /**
     * Calculates the fitness for each individual in the population. Here fitness function is represented by a total error for each individual.
     * Zero error means matrix is solved and it is the highest fitness. Higher error means lower fitness.
     * It also remember the last lowest error and calculates how many iteration it does not improve.
     * This is needed when the algorithm get stuck in local optimum and needs to be restarted.
     *
     * @param population Population to be evaluated with fitness.
     */
    private void calculateFitness(final List<Map.Entry<Matrix, Integer>> population) {
        // calculate total error for each matrix
        for (Map.Entry<Matrix, Integer> entry : population) {
            entry.setValue(entry.getKey().getTotalError());
        }
        // sort the population according to fitness
        population.sort(Map.Entry.comparingByValue());
        // increase iteration if best fitness is stagnating
        mCurrentMinimalError = population.get(0).getValue();
        if (mCurrentMinimalError == mLastMinimalError) {
            mStagnationIteration++;
        } else {
            mStagnationIteration = 0;
        }
        mLastMinimalError = mCurrentMinimalError;
    }

    /**
     * Mutates a percentage of population.
     *
     * @param population population to be mutated
     */
    private void mutate(final List<Map.Entry<Matrix, Integer>> population) {
        int randomIndex, randomMatrixRow, randomMatrixColumn, randomValue;
        // iterate
        for (int i = 0; i < population.size() * MUTATION_RATIO; i++) {
            randomIndex = mRandom.nextInt(population.size());
            // generate new random axis until one of allowed are found
            do {
                randomMatrixRow = mRandom.nextInt(MATRIX_LENGTH);
                randomMatrixColumn = mRandom.nextInt(MATRIX_LENGTH);
            } while (!checkIfNotInitialValue(randomMatrixRow, randomMatrixColumn));
            randomValue = mRandom.nextInt(MATRIX_LENGTH) + 1;
            population.get(randomIndex).getKey().setValue(randomMatrixRow, randomMatrixColumn, randomValue);
        }
    }

    /**
     * Creates an offspring by random crossover of parent1 and random parent2 from selected population.
     * Crossover is done for random number of either rows or columns.
     *
     * @param parent1             first parent
     * @param parent1             first parent
     * @param populationOffspring List where the offspring will be added.
     */
    private void crossover(final Matrix parent1,
                           final Matrix parent2,
                           final List<Map.Entry<Matrix, Integer>> populationOffspring) {

        final Matrix offspring1 = new Matrix(parent1.getArray());
        final Matrix offspring2 = new Matrix(parent2.getArray());
        final int randomCrossoverLine = mRandom.nextInt(MATRIX_LENGTH);
        final int randomCrossoverStyle = mRandom.nextInt(2);

        final int probabilityValue = mRandom.nextInt(9);
        // do crossover only with CROSSOVER_RATIO probability
        if (probabilityValue < (int) CROSSOVER_RATIO * 10) {
            // iterate random number of rows or columns => create offspring from parent1 and parent2
            for (int i = 0; i < randomCrossoverLine; i++) {
                if (randomCrossoverStyle == 0) {
                    offspring1.setRow(i, parent2.getRow(i));
                    offspring2.setRow(i, parent1.getRow(i));
                } else {
                    offspring1.setColumn(i, parent2.getColumn(i));
                    offspring2.setColumn(i, parent1.getColumn(i));
                }
            }
        }
        log.trace("OFFSPRING1: %s", offspring1.toString());
        log.trace("OFFSPRING2: %s", offspring2.toString());
        populationOffspring.add(new AbstractMap.SimpleEntry<Matrix, Integer>(offspring1, -1));
        populationOffspring.add(new AbstractMap.SimpleEntry<Matrix, Integer>(offspring2, -1));
    }

    /**
     * Creates new matrix with random values filled in empty cells
     *
     * @return random {@link Matrix} based on initial Matrix
     */
    private Matrix createRandomAllowedMatrix() {
        // create copy of initial matrix
        Matrix randomMatrix = new Matrix(mInitialMatrix.getArray());
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            for (int j = 0; j < MATRIX_LENGTH; j++) {
                if (checkIfNotInitialValue(i, j)) {
                    // update values with random where not initial value
                    randomMatrix.setValue(i, j, mRandom.nextInt(MATRIX_LENGTH) + 1);
                    log.trace("createRandomAllowedMatrix i={}, j={}, value={}", i, j, randomMatrix.getValue(i, j));
                }
            }
        }
        return randomMatrix;
    }
}
