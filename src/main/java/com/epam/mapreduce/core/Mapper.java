package com.epam.mapreduce.core;

public interface Mapper<I, K, V> {

    public void map(I input, Context<K, V> context);

}