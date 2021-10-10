package com.example.services;

import com.example.beans.CallbackInfo;
import com.example.db.DbComponent;
import com.example.tasks.CallbackTask;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
public class SubscriptionService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    @Inject
    DbComponent db;

    public boolean register(CallbackInfo callbackInfo) {

        log.info("Request to register : {}", callbackInfo);
        if(!db.uriExist(callbackInfo.getUri())) {
            ScheduledFuture<?> scheduler = scheduledExecutorService
                    .scheduleAtFixedRate(new CallbackTask(callbackInfo.getUri(), db),
                    0, callbackInfo.getFrequency(), TimeUnit.SECONDS);

            db.addNewScheduler(callbackInfo.getUri(), scheduler);
            return true;
        }

        return false;
    }

    public boolean unregister(String uriCallbackToCancel){

        log.info("Request to unregister : {}", uriCallbackToCancel);

        if(db.uriExist(uriCallbackToCancel)) {
            boolean result = db.getScheduler(uriCallbackToCancel).cancel(true);
            db.deleteScheduler(uriCallbackToCancel);
            return result;
        }

        return false;
    }

    public boolean update(CallbackInfo callbackRequest) {

        log.info("Request to update : {}", callbackRequest);

        boolean unregistered = unregister(callbackRequest.getUri());
        boolean registered = register(callbackRequest);

        return unregistered && registered;
    }
}
