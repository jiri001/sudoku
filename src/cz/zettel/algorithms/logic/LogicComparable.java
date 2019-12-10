package cz.zettel.algorithms.logic;

import com.sun.istack.internal.NotNull;
import cz.zettel.algorithms.entity.Matrix;

/**
 * This interface defines methods to be implemented in search logic in order to present the results
 */
public interface LogicComparable {
    /**
     * Returns initial matrix
     * @return
     */
    @NotNull
    Matrix getInitialMatrix();

    /**
     * Returns solved natrix
     * @return
     */
    @NotNull
    Matrix getCalculatedMatrix();

    /**
     * Returns measured time of computing.
     * @return
     */
    @NotNull
    Long getElapsedTime();
}
