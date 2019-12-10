package cz.zettel.algorithms.entity;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a structure holding a matrix of the values for Sudoku game.
 * It contains basic methods for the data manipulation in the matrix, they are used by the search algorithms.
 */
public class Matrix implements MatrixConstants {

    private static final Logger log = LoggerFactory.getLogger(Matrix.class.getName());

    /**
     * 2D array of integer values representing all the cells in the Matrix.
     */
    @NotNull
    private final int[][] mMatrixValues;

    /**
     * Default constructor.
     *
     * @param matrixValues 2D array of the cells
     */
    public Matrix(int[][] matrixValues) {
        mMatrixValues = new int[matrixValues.length][];
        for (int i = 0; i < matrixValues.length; i++)
            mMatrixValues[i] = matrixValues[i].clone();
    }

    @NotNull
    public int[][] getArray() {
        return mMatrixValues;
    }

    /**
     * Increases value of custom cell
     *
     * @param row
     * @param column
     * @return {@code true} is successfully increased, otherwise {@code false}
     */
    @NotNull
    public boolean increaseValue(final int row, final int column) {
        if (mMatrixValues[row][column] == MATRIX_LENGTH) {
            return false;
        } else {
            mMatrixValues[row][column] = mMatrixValues[row][column] + 1;
            return true;
        }
    }

    /**
     * Decreases value of custom cell
     *
     * @param row
     * @param column
     * @return {@code true} is successfully decreased, otherwise {@code false}
     */
    @NotNull
    public boolean decreaseValue(final int row, final int column) {
        if (mMatrixValues[row][column] == 1) {
            return false;
        } else if (mMatrixValues[row][column] == EMPTY_VALUE) {
            mMatrixValues[row][column] = MATRIX_LENGTH;
            return true;
        } else {
            mMatrixValues[row][column] = mMatrixValues[row][column] - 1;
            return true;
        }
    }

    public void setValue(final int row, final int column, final int value) {
        mMatrixValues[row][column] = value;
    }

    @NotNull
    public int getValue(final int row, final int column) {
        return mMatrixValues[row][column];
    }

    @NotNull
    public void deleteValue(final int row, final int column) {
        mMatrixValues[row][column] = EMPTY_VALUE;
    }

    @NotNull
    public int[] getRow(final int row) {
        return mMatrixValues[row];
    }

    @NotNull
    public int[] getColumn(final int column) {
        final int[] columnArray = new int[MATRIX_LENGTH];
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            columnArray[i] = mMatrixValues[i][column];
        }
        return columnArray;
    }

    @NotNull
    public void setRow(final int row, final int[] rowArray) {
        mMatrixValues[row] = rowArray.clone();
    }

    @NotNull
    public void setColumn(final int column, final int[] columnArray) {
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            mMatrixValues[i][column] = columnArray[i];
        }
    }

    /**
     * Checks if the value in the cell violates any constraint (axis X, axis Y, square)
     *
     * @param row
     * @param column
     * @return {@code true} is constraint violated, otherwise {@code false}
     */
    @NotNull
    public boolean isConstraintViolated(int row, int column) {
        return calculateTopOccurrence(getRowOccurrences(row)) > 1
                || calculateTopOccurrence(getColumnOccurrences(column)) > 1
                || calculateTopOccurrence(getSquareOccurrences(row, column)) > 1
                ? true : false;
    }

    /**
     * Calculates an error of the square where the cell belongs to.
     *
     * @param row
     * @param column
     * @return square error
     */
    @NotNull
    public int getSquareError(int row, int column) {
        return calculateError(getSquareOccurrences(row, column));
    }

    @NotNull
    public int getRowError(final int row) {
        return calculateError(getRowOccurrences(row));
    }

    @NotNull
    public int getColumnError(final int column) {
        return calculateError(getColumnOccurrences(column));
    }

    /**
     * Calculates total error for the current matrix.
     * It is a sum of error calculated for all the squares + rows + columns.
     *
     * @return Total error.
     */
    @NotNull
    public int getTotalError() {
        int totalError = 0;
        // calculate error of all the squares
        for (int i = 0; i < MATRIX_LENGTH; i += SQUARE_LENGTH) {
            for (int j = 0; j < MATRIX_LENGTH; j += SQUARE_LENGTH) {
                log.trace("getTotalError row = {}, column = {}", i, j);
                totalError += getSquareError(i, j);
            }
        }
        // calculate error of all the rows and columns
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            totalError += getRowError(i);
            totalError += getColumnError(i);
        }
        return totalError;
    }


    /**
     * Calculate occurrences of the values in a square where a cell belongs to.
     *
     * @param row
     * @param column
     * @return A vector of occurrences for each possible value = frequency histogram.
     */
    @NotNull
    private int[] getSquareOccurrences(final int row, final int column) {
        final int startIndexRow, startIndexColumn;
        final int occurrences[] = new int[MATRIX_LENGTH];

        // FIND STARTING INDICES

        // find division reminder
        if (row % SQUARE_LENGTH == 0) {
            startIndexRow = row;
        } else {
            // find division quotient
            startIndexRow = row / SQUARE_LENGTH * SQUARE_LENGTH;
        }
        // find division reminder
        if (column % SQUARE_LENGTH == 0) {
            startIndexColumn = column;
        } else {
            // find division quotient
            startIndexColumn = column / SQUARE_LENGTH * SQUARE_LENGTH;
        }
        log.trace("row = {}, column = {}", row, column);
        log.trace("startIndexRow = {}, startIndexColumn = {}", startIndexRow, startIndexColumn);


        int currentValue;
        for (int i = 0; i < SQUARE_LENGTH; i++) {
            for (int j = 0; j < SQUARE_LENGTH; j++) {
                currentValue = mMatrixValues[startIndexRow + i][startIndexColumn + j];
                addOccurrence(occurrences, currentValue);
            }
        }
        return occurrences;

    }

    /**
     * Calculate occurrences of the values in a row.
     *
     * @param row
     * @return A vector of occurrences for each possible value = frequency histogram.
     */
    @NotNull
    private int[] getRowOccurrences(final int row) {
        final int occurrences[] = new int[MATRIX_LENGTH];
        for (final int currentValue : mMatrixValues[row]) {
            addOccurrence(occurrences, currentValue);
        }
        return occurrences;
    }

    /**
     * Calculate occurrences of the values in a column.
     *
     * @param column
     * @return A vector of occurrences for each possible value = frequency histogram.
     */
    @NotNull
    private int[] getColumnOccurrences(final int column) {
        final int occurrences[] = new int[MATRIX_LENGTH];
        for (int i = 0; i < MATRIX_LENGTH; i++) {
            addOccurrence(occurrences, mMatrixValues[i][column]);
        }
        return occurrences;
    }

    /**
     * Calculates error from the frequency histogram vector.
     *
     * @param occurrences
     * @return Error calculated as number of missing values out of the enumeration.
     */
    @NotNull
    private int calculateError(final int[] occurrences) {
        int error = 0;
        // increase square error on each missing value
        for (int value : occurrences) {
            error = value == 0 ? error + 1 : error;
        }
        return error;
    }

    /**
     * Calculates top occurrence of any value in the occurrences vector.
     *
     * @param occurrences
     * @return Only the highest occurrence.
     */
    @NotNull
    private int calculateTopOccurrence(final int[] occurrences) {
        int result = 0;
        for (int value : occurrences) {
            result = value > result ? value : result;
        }
        return result;
    }

    /**
     * Adds an occurrence of a value to the occurrences vector.
     *
     * @param occurrences
     * @param currentValue
     * @return occurrences vector increased of occurrence given by the value
     */
    @NotNull
    private int[] addOccurrence(int[] occurrences, final int currentValue) {
        // calculate occurrences of values, do not calculate occurrences of initial zeroes
        if (currentValue != 0) {
            occurrences[currentValue - 1]++;
        }
        return occurrences;
    }

    @Override
    public String toString() {
        String matrix = "";
        for (int[] row : mMatrixValues) {
            matrix = matrix + System.lineSeparator() + Arrays.toString(row);
        }
        return matrix;
    }

}
