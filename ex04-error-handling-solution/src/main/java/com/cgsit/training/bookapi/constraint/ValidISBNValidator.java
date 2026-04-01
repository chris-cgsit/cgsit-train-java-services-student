package com.cgsit.training.bookapi.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidISBNValidator implements ConstraintValidator<ValidISBN, String> {

    private static final Pattern ISBN_13_PATTERN =
        Pattern.compile("^978-\\d{10}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotBlank handles null check
        }
        return ISBN_13_PATTERN.matcher(value).matches();
    }
}
