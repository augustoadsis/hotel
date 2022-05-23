package com.augusto.hotel.booking;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

//    I've used post instead get because it's receiving a request body (AvailabilityDTO) and validating it through annotations.
//    Get with request body works on postman and curl, but in some web frameworks and swagger it doesn't work.
    @PostMapping("/availabilities")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "List available dates to book")
    public Mono<List<LocalDate>> availabilities(@Valid @RequestBody AvailabilityDTO availability) {
        return bookingService.availabilities(availability);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Book the room")
    public void book(@Valid @RequestBody BookingDTO booking) {
        bookingService.save(booking);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Change reservation dates")
    public void update(@NotNull @PathVariable("id") Long id, @Valid @RequestBody BookingDTO booking) {
        bookingService.update(id, booking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Cancel reservation")
    public void cancel(@NotNull @PathVariable("id") Long id) {
        bookingService.cancel(id);
    }
}
