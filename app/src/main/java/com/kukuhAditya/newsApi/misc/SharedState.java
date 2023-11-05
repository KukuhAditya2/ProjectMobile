package com.kukuhAditya.newsApi.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedState {
    private static final SharedState instance = new SharedState();
    private SharedState(){}
    public static SharedState getInstance(){
        return instance;
    }

    private final Map<String, Object> storage = new ConcurrentHashMap<>();

    public void putSetting(String key, Object value){
        this.storage.put(key, value);
    }

    public Object getSetting(String key, Object defaultValue) {
        return storage.getOrDefault(key, defaultValue);
    }
}
