package com.epam.mapreduce.core;

public interface InputTokenizer<T>  {

    boolean hasNext();

    T next();

}