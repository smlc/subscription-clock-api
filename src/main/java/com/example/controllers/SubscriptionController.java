package com.example.controllers;

import com.example.beans.CallbackInfo;
import com.example.services.SubscriptionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

import java.util.Map;


@Controller("/subscription")
public class SubscriptionController {

    @Inject
    SubscriptionService subscriptionService;

    @Post("/register")
    public HttpResponse<String> register(@Body CallbackInfo callbackRequest) {
        boolean registered = subscriptionService.register(callbackRequest);

        if(registered) {
            return HttpResponse.ok();
        } else {
            return HttpResponse.badRequest(String.format("The uri %s already exist", callbackRequest.getUri()));
        }
    }

    @Post("/unregister")
    public HttpResponse<String> register(@Body Map<String, String> uriCallbackToCancel) {
        boolean unregistered = subscriptionService.unregister(uriCallbackToCancel.get("uri"));

        if(unregistered) {
            return HttpResponse.ok();
        } else {
            return HttpResponse.badRequest(String.format("The uri %s doesn't exist", uriCallbackToCancel.get("url")));
        }
    }

    @Put("/update")
    public HttpResponse<String> update(@Body CallbackInfo callbackRequest) {
        boolean updated = subscriptionService.update(callbackRequest);

        if(callbackRequest.getFrequency() < 5 || callbackRequest.getFrequency() > 14400) {
            return HttpResponse.badRequest(String.format("The given frequency %d, should be between 5 seconds and 4 hours.", callbackRequest.getFrequency()));
        }

        if(updated) {
            return HttpResponse.ok();
        } else {
            return HttpResponse.badRequest(String.format("We can't update the given uri %s", callbackRequest.getUri()));
        }
    }

}
