package com.cgsit.training.security.resource;

import com.cgsit.training.security.model.UserInfo;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Secured REST Resource — demonstrates Jakarta Security annotations.
 *
 * Security is enforced by WildFly Elytron via:
 *   1. web.xml        — security-constraint + BASIC login-config
 *   2. jboss-web.xml  — maps to Elytron security domain
 *   3. Annotations    — @RolesAllowed, @PermitAll, @DenyAll
 *
 * Users are defined in WildFly's application-users.properties:
 *   add-user.sh -a -u alice -p alice123 -g user
 *   add-user.sh -a -u bob   -p bob123   -g user,admin
 *
 * Or via WildFly CLI:
 *   /subsystem=elytron/properties-realm=application-realm:add(...)
 */
@Path("/secured")
@Produces(MediaType.APPLICATION_JSON)
public class SecuredResource {

    // SecurityContext is injected by JAX-RS runtime.
    // It provides the authenticated user's principal and role checks.
    // This is the REST equivalent of WebServiceContext in SOAP (03c example).
    @Context
    SecurityContext securityContext;

    /**
     * Any authenticated user can access this.
     * Shows who you are and what roles you have.
     */
    @GET
    @Path("/whoami")
    @RolesAllowed({"user", "admin"})
    public UserInfo whoAmI() {
        String username = securityContext.getUserPrincipal().getName();
        String authScheme = securityContext.getAuthenticationScheme();
        boolean secure = securityContext.isSecure();

        // Check which roles the user has
        List<String> roles = new ArrayList<>();
        if (securityContext.isUserInRole("user")) roles.add("user");
        if (securityContext.isUserInRole("admin")) roles.add("admin");
        if (securityContext.isUserInRole("manager")) roles.add("manager");

        return new UserInfo(username, authScheme, secure, roles);
    }

    /**
     * Only users with role "user" (or "admin") can access.
     */
    @GET
    @Path("/user-area")
    @RolesAllowed({"user", "admin"})
    public Map<String, String> userArea() {
        return Map.of(
            "message", "Welcome to the user area",
            "user", securityContext.getUserPrincipal().getName()
        );
    }

    /**
     * Only users with role "admin" can access.
     * A user with only role "user" gets 403 Forbidden.
     */
    @GET
    @Path("/admin-area")
    @RolesAllowed("admin")
    public Map<String, String> adminArea() {
        return Map.of(
            "message", "Welcome to the admin area",
            "admin", securityContext.getUserPrincipal().getName()
        );
    }

    /**
     * Combination: check role programmatically in business logic.
     * The endpoint is accessible to all authenticated users,
     * but the response differs based on the role.
     */
    @GET
    @Path("/dashboard")
    @RolesAllowed({"user", "admin"})
    public Map<String, Object> dashboard() {
        String user = securityContext.getUserPrincipal().getName();
        boolean isAdmin = securityContext.isUserInRole("admin");

        Map<String, Object> data = new java.util.LinkedHashMap<>();
        data.put("user", user);
        data.put("role", isAdmin ? "admin" : "user");
        data.put("items", List.of("Products", "Orders", "Reports"));

        // Admin sees extra menu items
        if (isAdmin) {
            data.put("adminItems", List.of("User Management", "System Config", "Audit Log"));
        }

        return data;
    }

    /**
     * @DenyAll — no one can access, regardless of role.
     * Useful for temporarily disabling an endpoint.
     */
    @GET
    @Path("/maintenance")
    @DenyAll
    public Map<String, String> maintenance() {
        return Map.of("message", "This should never be reached");
    }
}
