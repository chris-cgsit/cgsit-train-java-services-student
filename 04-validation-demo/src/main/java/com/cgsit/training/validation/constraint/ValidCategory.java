package com.cgsit.training.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCategoryValidator.class)
public @interface ValidCategory {

    String message() default "Ungueltige Kategorie. Erlaubt: Electronics, Peripherie, Software, Books";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
