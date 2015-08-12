package com.epam.mapreduce.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @param <I> input type
 * @param <K> key type
 * @param <V> value type
 */
public class MapReduce<I, K, V> {

    private static final int WAIT_TIME = 60;
    private static final int THREADS_COUNT = Runtime.getRuntime().availableProcessors();

    private Context<K, V> mapperContext = new Context();
    private Context<K, V> reducerContext = new Context<>();

    private InputTokenizer<I> inputTokenizer;
    private MapperFactory<Mapper<I, K, V>> mapperFactory;
    private ReducerFactory<Reducer<K, V>> reducerFactory;

    public MapReduce(InputTokenizer<I> inputTokenizer, MapperFactory<Mapper<I, K, V>> mapperFactory, ReducerFactory<Reducer<K, V>> reducerFactory) {
        this.inputTokenizer = inputTokenizer;
        this.mapperFactory = mapperFactory;
        this.reducerFactory = reducerFactory;
    }

    public Map<K, V> execute() throws InterruptedException {
        map();
        reduce(mapperContext.getContext());
        return convert();
    }

    private Map<K, V> convert() {
        Map<K, List<V>> context = reducerContext.getContext();
        return context.entrySet().stream().parallel().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get(0))
        );
    }

    private void map() throws InterruptedException {
        doInParallel(executor -> {
            while (inputTokenizer.hasNext()) {
                final I input = inputTokenizer.next();
                executor.submit(() -> {
                    Mapper<I, K, V> map = mapperFactory.create();
                    map.map(input, mapperContext);
                });
            }
        });
    }

    private void reduce(Map<K, List<V>> mapped) throws InterruptedException {
        doInParallel(executor -> {
            for (final Map.Entry<K, List<V>> entry : mapped.entrySet()) {
                executor.submit(() -> {
                    Reducer<K, V> red = reducerFactory.create();
                    red.reduce(entry.getKey(), entry.getValue(), reducerContext);
                });
            }
        });
    }

    private void doInParallel(ParallelTask parallelTask) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        parallelTask.process(executor);
        executor.shutdown();
        executor.awaitTermination(WAIT_TIME, TimeUnit.SECONDS);
    }

    private interface ParallelTask {
        void process(ExecutorService executorService);
    }

}