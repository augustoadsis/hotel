package com.augusto.hotel.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("FROM Booking WHERE checkIn BETWEEN :checkIn AND :checkOut")
    List<Booking> availabilities(LocalDate checkIn, LocalDate checkOut);
    @Query("FROM Booking WHERE :checkIn BETWEEN checkIn AND checkOut")
    List<Booking> checkAvailability(LocalDate checkIn);
}
