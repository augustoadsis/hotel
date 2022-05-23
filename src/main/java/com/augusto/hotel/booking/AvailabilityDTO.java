package com.augusto.hotel.booking;

import com.augusto.hotel.validation.DateValidation;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AvailabilityDTO {
    @DateValidation()
    LocalDate checkIn;
    LocalDate checkOut;
}
