package com.augusto.hotel.booking;

import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface BookingMapper {
    Booking toModel(BookingDTO dto);
    BookingDTO toDto(Booking model);
}
