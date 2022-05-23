package com.augusto.hotel.booking;

import com.augusto.hotel.customer.Customer;
import com.augusto.hotel.customer.CustomerDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public class BookingMocks {

    public static AvailabilityDTO availabilityDTOMock(LocalDate start, LocalDate end) {
        AvailabilityDTO availabilityDTO = new AvailabilityDTO();
        availabilityDTO.setCheckIn(start);
        availabilityDTO.setCheckOut(end);
        return availabilityDTO;
    }

    private static Customer customerMock() {
        Customer customer = new Customer();
        customer.setId(1L);
        return customer;
    }

    private static CustomerDTO customerMockDTO() {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(1L);
        return customer;
    }

    public static Booking bookingMock() {
        Customer customer = customerMock();
        Booking booking = new Booking();
        booking.setCheckIn(LocalDate.of(2022,5,20));
        booking.setCheckOut(LocalDate.of(2022,5,20));
        booking.setCustomer(customer);
        return booking;
    }

    public static Booking bookingWithIdMock() {
        Booking booking = bookingMock();
        booking.setId(1L);
        return booking;
    }

    public static BookingDTO bookingDTOMock() {
        CustomerDTO customer = customerMockDTO();
        BookingDTO booking = new BookingDTO();
        booking.setCheckIn(LocalDate.of(2022,5,20));
        booking.setCheckOut(LocalDate.of(2022,5,20));
        booking.setCustomer(customer);
        return booking;
    }

    public static List<Booking> availabilitiesMock() {
        Customer customer = customerMock();

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckIn(LocalDate.of(2022,5,20));
        booking.setCheckOut(LocalDate.of(2022,5,20));
        booking.setCustomer(customer);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setCheckIn(LocalDate.of(2022,5,21));
        booking2.setCheckOut(LocalDate.of(2022,5,23));
        booking2.setCustomer(customer);

        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setCheckIn(LocalDate.of(2022,5,24));
        booking3.setCheckOut(LocalDate.of(2022,5,25));
        booking3.setCustomer(customer);

        return List.of(booking,booking2,booking3);
    }

    public static List<LocalDate> availableDates() {
        return List.of(LocalDate.of(2022,5,26),
                LocalDate.of(2022,5,27),
                LocalDate.of(2022,5,28),
                LocalDate.of(2022,5,29));
    }

    public static Mono<List<LocalDate>> availableDatesCache() {
        return Mono.just(List.of(LocalDate.of(2022,5,26),
                LocalDate.of(2022,5,27),
                LocalDate.of(2022,5,28),
                LocalDate.of(2022,5,29)));
    }

}
