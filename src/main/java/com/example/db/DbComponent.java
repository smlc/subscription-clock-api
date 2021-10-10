package com.example.db;

import jakarta.inject.Singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Singleton
public class DbComponent {

    Map<String, ScheduledFuture> db = new ConcurrentHashMap<>();

    public boolean uriExist(String uri) {
        return db.containsKey(uri);
    }

    public ScheduledFuture<?> getScheduler(String uri) {
        return db.get(uri);
    }

    public void deleteScheduler(String uri) {
        db.remove(uri);
    }

    public void  addNewScheduler(String uri, ScheduledFuture<?> scheduledFuture) {
        db.put(uri, scheduledFuture);
    }
}
