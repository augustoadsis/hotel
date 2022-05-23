package com.augusto.hotel.booking;

import com.augusto.hotel.customer.CustomerDTO;
import com.augusto.hotel.validation.DateRangeValidation;
import com.augusto.hotel.validation.DateValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DateRangeValidation
public class BookingDTO implements Serializable {
    Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateValidation
    LocalDate checkIn;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate checkOut;
    CustomerDTO customer;
}
