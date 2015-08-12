package com.epam.mapreduce;

import com.epam.mapreduce.core.Context;
import com.epam.mapreduce.core.Mapper;

import java.util.StringTokenizer;

public class WordCountMapper implements Mapper<String, String, Integer> {

    @Override
    public void map(String input, Context<String, Integer> context) {
        StringTokenizer stringTokenizer = new StringTokenizer(input);
        while(stringTokenizer.hasMoreTokens()) {
            context.put(stringTokenizer.nextToken(), 1);
        }
    }

}