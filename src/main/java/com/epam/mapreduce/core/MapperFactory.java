package com.epam.mapreduce.core;

public interface MapperFactory<T extends Mapper> {

    T create();

}