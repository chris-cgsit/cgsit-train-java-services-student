package com.cgsit.training.config.resource;

import com.cgsit.training.config.service.AppConfig;
import com.cgsit.training.config.service.NotificationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * REST Resource that exposes configuration values.
 *
 * Demonstrates:
 *   - Injected config via AppConfig bean
 *   - Programmatic config via ConfigProvider
 *   - Config override via environment variables
 *
 * Test by setting environment variables before starting WildFly:
 *   set APP_NAME=Production App
 *   set APP_ENVIRONMENT=production
 *   set SMTP_HOST=mail.prod.example.com
 *   set FEATURE_DARKMODE=true
 */
@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @Inject
    AppConfig appConfig;

    @Inject
    NotificationService notificationService;

    /**
     * GET /api/config — show all injected config values.
     *
     * Change environment variables and restart to see different values.
     */
    @GET
    public Map<String, Object> showConfig() {
        Map<String, Object> config = new LinkedHashMap<>();

        // Application
        config.put("app.name", appConfig.getAppName());
        config.put("app.version", appConfig.getAppVersion());
        config.put("app.environment", appConfig.getEnvironment());
        config.put("app.description", appConfig.getDescription().orElse("(not set)"));

        // SMTP
        config.put("smtp.host", appConfig.getSmtpHost());
        config.put("smtp.port", appConfig.getSmtpPort());
        config.put("smtp.from", appConfig.getSmtpFrom());

        // Database
        config.put("db.url", appConfig.getDbUrl());
        config.put("db.user", appConfig.getDbUser());

        // Feature Flags
        config.put("feature.darkmode", appConfig.isDarkMode());
        config.put("feature.notifications", appConfig.isNotificationsEnabled());
        config.put("feature.max-items-per-page", appConfig.getMaxItemsPerPage());

        // List
        config.put("app.allowed-origins", appConfig.getAllowedOrigins());

        return config;
    }

    /**
     * GET /api/config/source/{key} — show where a config value comes from.
     *
     * Demonstrates programmatic access via ConfigProvider.
     */
    @GET
    @Path("/source/{key}")
    public Map<String, String> showSource(@PathParam("key") String key) {
        Config config = ConfigProvider.getConfig();

        Map<String, String> result = new LinkedHashMap<>();
        result.put("key", key);
        try {
            String value = config.getValue(key, String.class);
            result.put("value", value);
            result.put("source", "resolved (highest priority wins)");
        } catch (java.util.NoSuchElementException e) {
            result.put("value", "(not found)");
            result.put("source", "none");
        }

        return result;
    }

    /**
     * GET /api/config/all-properties — list all known config property names.
     */
    @GET
    @Path("/all-properties")
    public Map<String, String> allProperties() {
        Config config = ConfigProvider.getConfig();
        Map<String, String> props = new TreeMap<>();

        for (String name : config.getPropertyNames()) {
            // Only show our app properties, skip system/WildFly internals
            if (name.startsWith("app.") || name.startsWith("smtp.")
                    || name.startsWith("db.") || name.startsWith("feature.")) {
                try {
                    props.put(name, config.getValue(name, String.class));
                } catch (Exception e) {
                    props.put(name, "(error reading)");
                }
            }
        }
        return props;
    }

    /**
     * POST /api/config/notify — test notification service with config values.
     */
    @POST
    @Path("/notify")
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, String> sendNotification(Map<String, String> body) {
        String to = body.getOrDefault("to", "test@example.com");
        String message = body.getOrDefault("message", "Hello from Config Demo");

        String result = notificationService.sendNotification(to, message);

        return Map.of(
            "result", result,
            "smtp.host", appConfig.getSmtpHost(),
            "feature.notifications", String.valueOf(appConfig.isNotificationsEnabled())
        );
    }
}
