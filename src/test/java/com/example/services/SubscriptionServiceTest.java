package com.example.services;

import com.example.beans.CallbackInfo;
import com.example.db.DbComponent;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MicronautTest
public class SubscriptionServiceTest {

    @Mock
    DbComponent dbComponent;

    @InjectMocks
    SubscriptionService subscriptionService;

    @Test
    public void itShouldRegisterCallback() {

       boolean registred = subscriptionService.register(new CallbackInfo("http://localhost:8081/api/test2", 2));

        assertTrue(registred);
        verify(dbComponent, times(1)).addNewScheduler(eq("http://localhost:8081/api/test2"), any(ScheduledFuture.class));
    }

    @Test
    public void itShouldUnregisterCallback() {

        when(dbComponent.uriExist("http://localhost:8081/api/test2"))
                .thenReturn(true);

        var mockFuture = mock(ScheduledFuture.class);
        when(mockFuture.cancel(true))
                .thenReturn(true);
        when(dbComponent.getScheduler("http://localhost:8081/api/test2"))
                .thenReturn(mockFuture);

        boolean unregistred = subscriptionService.unregister("http://localhost:8081/api/test2");

        assertTrue(unregistred);
        verify(dbComponent, times(1)).getScheduler(eq("http://localhost:8081/api/test2"));
        verify(dbComponent, times(1)).deleteScheduler(eq("http://localhost:8081/api/test2"));
    }

    @Test
    public void itShouldUpdateCallback() {

        when(dbComponent.uriExist("http://localhost:8081/api/test2"))
                .thenReturn(true)
                .thenReturn(false);

        var mockFuture = mock(ScheduledFuture.class);
        when(mockFuture.cancel(true))
                .thenReturn(true);
        when(dbComponent.getScheduler("http://localhost:8081/api/test2"))
                .thenReturn(mockFuture);

        boolean updated = subscriptionService.update(new CallbackInfo("http://localhost:8081/api/test2", 2));

        assertTrue(updated);
        verify(dbComponent, times(1)).addNewScheduler(eq("http://localhost:8081/api/test2"), any(ScheduledFuture.class));
        verify(dbComponent, times(1)).getScheduler(eq("http://localhost:8081/api/test2"));
        verify(dbComponent, times(1)).deleteScheduler(eq("http://localhost:8081/api/test2"));
    }
}
