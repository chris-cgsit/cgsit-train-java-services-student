package com.cgsit.training.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonPropertyOrder({"id", "name", "email", "phone", "company", "createdAt"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Customer with validation and Jackson annotations")
public record Customer(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Customer ID (auto-generated)", example = "1", readOnly = true)
    Long id,

    @NotBlank(message = "Name darf nicht leer sein")
    @Size(min = 2, max = 100, message = "Name muss zwischen 2 und 100 Zeichen lang sein")
    @Schema(description = "Full name", example = "Anna Meier")
    String name,

    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "Ungueltige E-Mail-Adresse")
    @Schema(description = "Email address (unique)", example = "anna@example.com")
    String email,

    @NotBlank(message = "Telefonnummer darf nicht leer sein")
    @Pattern(regexp = "\\+\\d{1,3}\\s\\d{2,4}\\s\\d{5,10}",
             message = "Telefonnummer muss Format +XX XXX XXXXXXX haben")
    @Schema(description = "Phone number (+43 664 1234567)", example = "+43 664 1234567")
    String phone,

    @Size(max = 100, message = "Firmenname darf maximal 100 Zeichen lang sein")
    @Schema(description = "Company name (optional)", example = "CGS IT Solutions")
    String company,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Creation timestamp", readOnly = true)
    LocalDateTime createdAt
) {}
