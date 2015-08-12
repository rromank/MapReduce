package com.epam.mapreduce.core;

import java.util.List;

public interface Reducer<K, V> {

    public void reduce(K key, List<V> values, Context<K, V> context);

}