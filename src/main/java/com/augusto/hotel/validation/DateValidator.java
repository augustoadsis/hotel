package com.augusto.hotel.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateValidator implements ConstraintValidator<DateValidation, LocalDate> {
    public boolean isValid(LocalDate date, ConstraintValidatorContext cxt) {
        long days = DAYS.between(LocalDate.now(), date);
        return days <= 30;
    }
}