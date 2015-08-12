package com.epam.mapreduce.core;

public interface ReducerFactory<T extends Reducer> {

    T create();

}