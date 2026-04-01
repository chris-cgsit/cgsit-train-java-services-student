package com.cgsit.training.security.model;

import java.util.List;

/**
 * Response object showing the authenticated user's identity.
 * Demonstrates what SecurityContext provides at runtime.
 */
public record UserInfo(
    String username,
    String authScheme,
    boolean secure,
    List<String> roles
) {}
