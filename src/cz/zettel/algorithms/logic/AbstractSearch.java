package cz.zettel.algorithms.logic;

import com.sun.istack.internal.NotNull;
import cz.zettel.algorithms.entity.Matrix;
import cz.zettel.algorithms.entity.MatrixConstants;

/**
 * This class holds common features of any search algorithm.
 *
 */
public abstract class AbstractSearch implements LogicComparable, MatrixConstants {

    @NotNull
    Matrix mInitialMatrix;
    Matrix mSolvedMatrix;
    Long mStartTime, mElapsedTime;

    /**
     * Default constructor.
     *
     * @param initialMatrix Matrix to be solved. Missing celles are represented by zeroes.
     */
    public AbstractSearch(@NotNull final Matrix initialMatrix) {
        mStartTime = System.currentTimeMillis();
        mInitialMatrix = initialMatrix;
    }

    /**
     * This default method calculates the elapsed time. To be called after matrix calculation is done.
     */
    public void stopMeasuringTime() {
        mElapsedTime = System.currentTimeMillis() - mStartTime;
    }

    @Override
    public final Matrix getInitialMatrix() {
        return mInitialMatrix;
    }

    @Override
    public final Matrix getCalculatedMatrix() {
        return mSolvedMatrix;
    }

    /**
     * This default method returns the elapsed time.
     *
     * @return Elapsed time or -1 value if time was not calculated.
     */
    @Override
    public final Long getElapsedTime() {
        return mElapsedTime == null ? -1L : mElapsedTime;
    }

    /**
     * Checks if given cell is not the initial one which cannot be changed.
     *
     * @param row
     * @param column
     * @return {@code true} if the cell can be changed, otherwise {@code false}
     */
    boolean checkIfNotInitialValue(final int row, final int column) {
        return mInitialMatrix.getValue(row, column) == 0 ? true : false;
    }

    /**
     * Abstract method that will contain logic.
     */
    abstract void calculateMatrix();
}
