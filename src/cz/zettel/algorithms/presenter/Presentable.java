package cz.zettel.algorithms.presenter;

import cz.zettel.algorithms.logic.LogicComparable;

/**
 * This interface defines methods each presenter needs to implement
 */
public interface Presentable {
    /**
     * Print text unformatted.
     *
     * @param text
     */
    void print(final String text);

    /**
     * Pretty print of original matrix, solved matrix and some data about performance.
     *
     * @param compare Any object implementing {@link LogicComparable} interface.
     */
    void printComparison(final LogicComparable compare);
}
