package cz.zettel.algorithms.logic;

import cz.zettel.algorithms.entity.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a full search in the solutions tree. It starts with setting the value to 1 from the top-left
 * of the matrix and checks if any constraint is violated. If so, the value in increased by 1 until the constraint is OK.
 * If maximum number is achieved (all values from 1 to 9 violate the constraint), the value is deleted and previous value
 * is increased.
 */
public class FullSearch extends AbstractSearch {
    private static final Logger log = LoggerFactory.getLogger(FullSearch.class.getName());
    private static final int MIN_POINTER = 0;
    private static final int MAX_POINTER = MATRIX_LENGTH * MATRIX_LENGTH - 1;

    private int mPointer = MIN_POINTER;
    private boolean mIsMovingForward = true;

    public FullSearch(final Matrix initialMatrix) {
        super(initialMatrix);
        calculateMatrix();
        stopMeasuringTime();
    }

    @Override
    void calculateMatrix() {

        int currentRow, currentColumn;
        boolean wasCurrentTrySuccessful;

        // create copy of initial matrix
        mSolvedMatrix = new Matrix(mInitialMatrix.getArray());

        while (mPointer <= MAX_POINTER) {
            currentRow = getPointerRow();
            currentColumn = getPointerColumn();
            wasCurrentTrySuccessful = false;
            log.trace("pointer = {}, pointer row = {}, pointer column = {}", mPointer, currentRow, currentColumn);
            log.trace("current value = {}", mSolvedMatrix.getValue(currentRow, currentColumn));

            if (checkIfNotInitialValue(currentRow, currentColumn)) {
                // value increased (value < 9); can be replaced with decreaseValue to start searching from max number
                while (mSolvedMatrix.increaseValue(currentRow, currentColumn)) {
                    // check for constraint if OK
                    if (!mSolvedMatrix.isConstraintViolated(currentRow, currentColumn)) {
                        movePointerForward();
                        wasCurrentTrySuccessful = true;
                        break;
                    }
                }
                // value increased to 9 but constraint violated, end of loop
                if (!wasCurrentTrySuccessful) {
                    mSolvedMatrix.deleteValue(currentRow, currentColumn);
                    movePointerBackwards();
                }
            } else {
                // move pointer in current direction when current value is initial value
                movePointer();
            }

            log.trace("Going {}, moved pointer to {}, value = {}", mIsMovingForward ? "right" : "left",
                    mPointer, mSolvedMatrix.getValue(currentRow, currentColumn));
        }
    }

    private int getPointerRow() {
        return mPointer / MATRIX_LENGTH;
    }

    private int getPointerColumn() {
        return mPointer % MATRIX_LENGTH;
    }

    private int movePointer() {
        return mPointer = mIsMovingForward ? mPointer + 1 : mPointer - 1;
    }

    private int movePointerBackwards() {
        mIsMovingForward = false;
        return mPointer--;
    }

    private int movePointerForward() {
        mIsMovingForward = true;
        return mPointer++;
    }

}
