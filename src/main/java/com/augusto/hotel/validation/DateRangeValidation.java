package com.augusto.hotel.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})
public @interface DateRangeValidation {
    String message() default "Invalid date: cannot be booked for more than 3 days, please enter a valid period.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}