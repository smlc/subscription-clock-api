package com.example.tasks;

import com.example.db.DbComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CallbackTask implements Runnable {

    private final Logger log = LoggerFactory.getLogger(CallbackTask.class);

    private URI uri;
    DbComponent db;

    public CallbackTask(String uri, DbComponent db) {
        this.uri = URI.create(uri);
        this.db = db;
    }

    @Override
    public void run() {

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(3))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                    .build();

            HttpClient.newBuilder()
                    .build()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApplyAsync((resp) -> {
                        int status = resp.statusCode();
                        log.debug(String.format("Receive status code %d for %s", status, uri));
                        if (status != 200) {
                            log.error(String.format("Cancel the task for %s.", uri));
                            db.getScheduler(uri.toString()).cancel(true);
                            db.deleteScheduler(uri.toString());
                        }
                        return resp;
                    })
                    .exceptionally(err -> {
                        db.getScheduler(uri.toString()).cancel(true);
                        db.deleteScheduler(uri.toString());
                        log.error(String.format("Error while send request for %s, gonna cancel the task. ", uri));
                        log.error("Error: " + err.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

}
