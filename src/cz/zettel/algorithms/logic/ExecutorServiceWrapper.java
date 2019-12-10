package cz.zettel.algorithms.logic;

import com.sun.istack.internal.NotNull;
import cz.zettel.algorithms.entity.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceWrapper {
    private static final Logger log = LoggerFactory.getLogger(ExecutorService.class.getName());
    private final ExecutorService mExecutorService;
    private List<Future<LogicComparable>> mFuturesList;


    public ExecutorServiceWrapper(@NotNull final int nThreads) {
        mExecutorService = Executors.newFixedThreadPool(nThreads);
    }

    public void submitGeneticCallables(@NotNull final Matrix matrixInitial,
                                       @NotNull final int populationSize,
                                       @NotNull final float elitismRatio) throws InterruptedException {
        Collection<Callable<LogicComparable>> callables = new ArrayList<>();
        mFuturesList = mExecutorService.invokeAll(callables);
        for (int i = 0; i < ((ThreadPoolExecutor) mExecutorService).getCorePoolSize(); i++) {
            final Callable<LogicComparable> callable = new Callable<LogicComparable>() {
                @Override
                public LogicComparable call() throws Exception {
                    log.info("Starting thread: {}", Thread.currentThread().getName());
                    GeneticSearch geneticSearch = new GeneticSearch(matrixInitial, populationSize, elitismRatio);
                    return geneticSearch;
                }
            };
            Future<LogicComparable> future = mExecutorService.submit(callable);
            mFuturesList.add(future);
        }
    }

    public LogicComparable monitorThreadsAndGetResult() throws InterruptedException, ExecutionException {
        Iterator<Future<LogicComparable>> futuresIterator = mFuturesList.iterator();
        Future<LogicComparable> future = null;
        do {
            if (futuresIterator.hasNext()) {
                future = futuresIterator.next();
            } else {
                // restart iterator
                futuresIterator = mFuturesList.iterator();
            }
            Thread.sleep(20000);
        } while (!future.isDone());
        mExecutorService.shutdownNow();
        return future.get();
    }

}
