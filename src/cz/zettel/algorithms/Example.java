package cz.zettel.algorithms;

import cz.zettel.algorithms.entity.Matrix;
import cz.zettel.algorithms.logic.ExecutorServiceWrapper;
import cz.zettel.algorithms.logic.FullSearch;
import cz.zettel.algorithms.logic.GeneticSearch;
import cz.zettel.algorithms.presenter.Presentable;
import cz.zettel.algorithms.presenter.ConsolePresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;


public class Example {
    private static final Logger log = LoggerFactory.getLogger(Example.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("logback.configurationFile", "logback.xml");

        final int[][] example1 = new int[][]{
                {0, 1, 3, 8, 0, 0, 4, 0, 5},
                {0, 2, 4, 6, 0, 5, 0, 0, 0},
                {0, 8, 7, 0, 0, 0, 9, 3, 0},
                {4, 9, 0, 3, 0, 6, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 5, 0, 0},
                {0, 0, 0, 7, 0, 1, 0, 9, 3},
                {0, 6, 9, 0, 0, 0, 7, 4, 0},
                {0, 0, 0, 2, 0, 7, 6, 8, 0},
                {1, 0, 2, 0, 0, 8, 3, 5, 0}};

        final int[][] example2 = new int[][]{
                {0, 0, 2, 0, 0, 0, 0, 4, 1},
                {0, 0, 0, 0, 8, 2, 0, 7, 0},
                {0, 0, 0, 0, 4, 0, 0, 0, 9},
                {2, 0, 0, 0, 7, 9, 3, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 8, 0},
                {0, 0, 6, 8, 1, 0, 0, 0, 4},
                {1, 0, 0, 0, 9, 0, 0, 0, 0},
                {0, 6, 0, 4, 3, 0, 0, 0, 0},
                {8, 5, 0, 0, 0, 0, 4, 0, 0}};

        final int[][] example3 = new int[][]{
                {0, 0, 0, 3, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 9, 0, 8, 5, 0, 6, 3},
                {9, 0, 0, 0, 0, 0, 6, 7, 0},
                {0, 0, 0, 0, 4, 0, 0, 3, 0},
                {0, 1, 0, 8, 2, 0, 0, 0, 9},
                {0, 0, 5, 0, 0, 0, 0, 9, 0},
                {0, 0, 0, 0, 0, 6, 1, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 4}};

        Presentable presenter = new ConsolePresenter();
        final Matrix matrix1 = new Matrix(example1);
        final Matrix matrix2 = new Matrix(example2);
        final Matrix matrix3 = new Matrix(example3);
        FullSearch fullSearch;
        GeneticSearch geneticSearch;
        ExecutorServiceWrapper geneticSearchExecutor;



        // example1 using full search
        fullSearch = new FullSearch(matrix1);
        presenter.printComparison(fullSearch);

        // example2 using full search
        fullSearch = new FullSearch(matrix2);
        presenter.printComparison(fullSearch);

        // example3 using full search
        fullSearch = new FullSearch(matrix3);
        presenter.printComparison(fullSearch);

        // example1 using genetic search
        geneticSearch = new GeneticSearch(matrix1, 10000, 0.2F);
        presenter.printComparison(geneticSearch);

        // example2 using genetic search
        geneticSearch = new GeneticSearch(matrix2, 100000, 0.2F);
        presenter.printComparison(geneticSearch);

        // example2 using genetic search in parallel threads
        geneticSearchExecutor = new ExecutorServiceWrapper(3);
        geneticSearchExecutor.submitGeneticCallables(matrix2, 100000, 0.2F);
        presenter.printComparison(geneticSearchExecutor.monitorThreadsAndGetResult());

    }
}
