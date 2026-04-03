package com.cgsit.training.config.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Example service that uses @ConfigProperty directly.
 *
 * Shows that config injection works in any CDI bean,
 * not just in a centralized config class.
 */
@ApplicationScoped
public class NotificationService {

    @Inject
    @ConfigProperty(name = "smtp.host")
    String smtpHost;

    @Inject
    @ConfigProperty(name = "smtp.port")
    int smtpPort;

    @Inject
    @ConfigProperty(name = "smtp.from")
    String fromAddress;

    @Inject
    @ConfigProperty(name = "feature.notifications")
    boolean enabled;

    public String sendNotification(String to, String message) {
        if (!enabled) {
            return "Notifications disabled (feature.notifications=false)";
        }
        // Simulated send — in real app this would use JavaMail
        return String.format("Sent via %s:%d from %s to %s: %s",
                smtpHost, smtpPort, fromAddress, to, message);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
