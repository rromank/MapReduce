package com.epam.mapreduce.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Context<K, V> {

    private Map<K, List<V>> context = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        List<V> values = context.get(key);
        if (values != null) {
            values.add(value);
        } else {
            List<V> list = Collections.synchronizedList(new ArrayList<>());
            list.add(value);
            context.put(key, list);
        }
    }

    public Map<K, List<V>> getContext() {
        return context;
    }

    @Override
    public String toString() {
        return context.toString();
    }

}