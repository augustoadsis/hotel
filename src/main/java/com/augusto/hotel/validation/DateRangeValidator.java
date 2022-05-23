package com.augusto.hotel.validation;

import com.augusto.hotel.booking.BookingDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateRangeValidator implements ConstraintValidator<DateRangeValidation, BookingDTO> {
    public boolean isValid(BookingDTO bookingDTO, ConstraintValidatorContext cxt) {
        long days = DAYS.between(bookingDTO.getCheckIn(), bookingDTO.getCheckOut());
        return days <= 3;
    }
}