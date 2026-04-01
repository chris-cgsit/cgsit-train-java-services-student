package com.cgsit.training.validation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class ValidCategoryValidator implements ConstraintValidator<ValidCategory, String> {

    private static final Set<String> ALLOWED_CATEGORIES =
        Set.of("Electronics", "Peripherie", "Software", "Books");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotBlank handles null check
        }
        return ALLOWED_CATEGORIES.contains(value);
    }
}
