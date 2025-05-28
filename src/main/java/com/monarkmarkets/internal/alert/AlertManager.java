package com.monarkmarkets.internal.alert;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AlertManager {

    private final AlertManagerConfig config;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlertManager(AlertManagerConfig config) {
        this.config = config;
    }

    public void sendAlert(SendAlertOptions options) {
        if (!isConfigured()) {
            System.err.println("AlertManager not configured (URL, user, or password missing). Skipping alert.");
            return;
        }

        Alert alert = createAlert(options);
        try {
            String fullUrl = config.getUrl() + "/api/v2/alerts";
            String auth = Base64.getEncoder()
                    .encodeToString((config.getUser() + ":" + config.getPassword()).getBytes());
            String jsonBody = objectMapper.writeValueAsString(Collections.singletonList(alert));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + auth)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Alert '" + options.getTitle() + "' sent successfully.");
            } else {
                System.err.printf("Failed to send alert. Status: %d, Response: %s%n", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            System.err.println("Error sending alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Alert createAlert(SendAlertOptions options) {
        Alert alert = new Alert();
        alert.setLabels(new AlertLabels(
            options.getTitle().replaceAll("[ :]", "_"),
            options.getSeverity() != null ? options.getSeverity() : "warning",
            "custom",
            options.getSourceComponent() != null ? options.getSourceComponent() : "UnknownComponent",
            options.getEnvironment() != null ? options.getEnvironment() : "unknown"
        ));

        AlertAnnotations annotations = new AlertAnnotations();
        annotations.setTitle("[" + alert.getLabels().getEnvironment().toUpperCase() + "] " + options.getTitle());
        annotations.setMessage(options.getMessage());
        annotations.setDetails(options.getDetails());
        annotations.setAction(options.getAction());
        annotations.setLinkUrl(options.getLinkUrl());
        annotations.setLinkText(options.getLinkText());
        alert.setAnnotations(annotations);
        return alert;
    }

    private boolean isConfigured() {
        return config.getUrl() != null &&
               config.getUser() != null &&
               config.getPassword() != null;
    }
}
