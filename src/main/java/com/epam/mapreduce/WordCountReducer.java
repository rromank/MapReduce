package com.epam.mapreduce;

import com.epam.mapreduce.core.Context;
import com.epam.mapreduce.core.Reducer;

import java.util.List;

public class WordCountReducer implements Reducer<String, Integer> {

    @Override
    public void reduce(String key, List<Integer> values, Context<String, Integer> context) {
        int sum = 0;
        for (Integer count : values) {
            if (count != null) {
                sum += count;
            }
        }
        context.put(key, sum);
    }

}