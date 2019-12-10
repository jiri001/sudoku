package cz.zettel.algorithms.presenter;

import cz.zettel.algorithms.logic.LogicComparable;

public class ConsolePresenter implements Presentable {

    @Override
    public void print(final String text) {
        System.out.println(text);
    }

    @Override
    public void printComparison(final LogicComparable compare) {
        System.out.println();
        System.out.println("############ START REPORT " + compare.getClass().getSimpleName() + " ############");
        System.out.print("************ ORIGINAL MATRIX ************");
        System.out.println(compare.getInitialMatrix().toString());
        System.out.print("************ CALCULATED MATRIX ************");
        System.out.println(compare.getCalculatedMatrix().toString());
        System.out.println("Elapsed time: " + compare.getElapsedTime() + "ms");
        System.out.println("############ END REPORT " + compare.getClass().getSimpleName() + " ############");
        System.out.println();
    }

}
