package com.epam.mapreduce;

import com.epam.mapreduce.core.InputTokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileWordTokenizer implements InputTokenizer<String> {

    private BufferedReader bufferedReader;
    private String line;

    public FileWordTokenizer(String filePath) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
    }

    @Override
    public boolean hasNext() {
        readNextLine();
        return line != null;
    }

    @Override
    public String next() {
        readNextLine();
        String ret = line;
        line = null;
        return ret;
    }

    private void readNextLine() {
        try {
            if (line == null) {
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}