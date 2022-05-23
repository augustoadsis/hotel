package com.augusto.hotel.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DateValidator.class})
public @interface DateValidation {
    String message() default "Invalid date: cannot be booked 30 days in advance, please enter a valid period.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}