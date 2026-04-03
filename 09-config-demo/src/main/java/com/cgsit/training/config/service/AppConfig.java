package com.cgsit.training.config.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Centralized configuration bean — all @ConfigProperty injections in one place.
 *
 * Values come from (highest priority wins):
 *   1. System Properties   (-Dapp.name=...)         ordinal 400
 *   2. Environment Variables (APP_NAME=...)          ordinal 300
 *   3. microprofile-config.properties (in WAR)       ordinal 100
 *   4. defaultValue in annotation                    fallback
 *
 * Type conversion is automatic:
 *   String → int, boolean, long, List<String>, Optional<String>, ...
 */
@ApplicationScoped
public class AppConfig {

    // --- Application ---

    @Inject
    @ConfigProperty(name = "app.name", defaultValue = "Unnamed App")
    String appName;

    @Inject
    @ConfigProperty(name = "app.version", defaultValue = "0.0.0")
    String appVersion;

    @Inject
    @ConfigProperty(name = "app.environment", defaultValue = "unknown")
    String environment;

    // Optional — property may not exist (no error if missing)
    @Inject
    @ConfigProperty(name = "app.description")
    Optional<String> description;

    // --- SMTP ---

    @Inject
    @ConfigProperty(name = "smtp.host", defaultValue = "localhost")
    String smtpHost;

    @Inject
    @ConfigProperty(name = "smtp.port", defaultValue = "587")
    int smtpPort;

    @Inject
    @ConfigProperty(name = "smtp.from", defaultValue = "noreply@example.com")
    String smtpFrom;

    // --- Database ---

    @Inject
    @ConfigProperty(name = "db.url")
    String dbUrl;

    @Inject
    @ConfigProperty(name = "db.user")
    String dbUser;

    // --- Feature Flags ---

    @Inject
    @ConfigProperty(name = "feature.darkmode", defaultValue = "false")
    boolean darkMode;

    @Inject
    @ConfigProperty(name = "feature.notifications", defaultValue = "true")
    boolean notificationsEnabled;

    @Inject
    @ConfigProperty(name = "feature.max-items-per-page", defaultValue = "20")
    int maxItemsPerPage;

    // --- List (comma-separated in properties file) ---

    @Inject
    @ConfigProperty(name = "app.allowed-origins")
    List<String> allowedOrigins;

    // --- Getters ---

    public String getAppName() { return appName; }
    public String getAppVersion() { return appVersion; }
    public String getEnvironment() { return environment; }
    public Optional<String> getDescription() { return description; }
    public String getSmtpHost() { return smtpHost; }
    public int getSmtpPort() { return smtpPort; }
    public String getSmtpFrom() { return smtpFrom; }
    public String getDbUrl() { return dbUrl; }
    public String getDbUser() { return dbUser; }
    public boolean isDarkMode() { return darkMode; }
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public int getMaxItemsPerPage() { return maxItemsPerPage; }
    public List<String> getAllowedOrigins() { return allowedOrigins; }
}
