package com.augusto.hotel.booking;

import com.augusto.hotel.cache.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.augusto.hotel.booking.BookingMocks.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepositoryMock;

    @Mock
    BookingService bookingServiceMock;

    @Mock
    BookingMapper bookingMapperMock;

    @Mock
    CacheService cacheServiceMock;

    @InjectMocks
    BookingService bookingService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ExecutableValidator executableValidator = validator.forExecutables();

    @Test
    void getAvailabilities() {
        List<Booking> bookings = availabilitiesMock();
        LocalDate start = LocalDate.of(2022, 5, 20);
        LocalDate end = LocalDate.of(2022, 5, 30);
        AvailabilityDTO availabilityDTO = availabilityDTOMock(start, end);

        when(bookingRepositoryMock.availabilities(start, end)).thenReturn(bookings);
        Mono<List<LocalDate>> availabilities = bookingService.availabilities(availabilityDTO);

        StepVerifier
                .create(availabilities)
                .consumeNextWith(dates -> {
                    assertEquals( 4, dates.size());
                    assertEquals(availableDates(), dates);
                })
                .expectNextCount(0)
                .verifyComplete();

        verify(bookingRepositoryMock, times(1)).availabilities(start,end);
    }

    @Test
    void getAvailabilitiesCache() {
        LocalDate start = LocalDate.of(2022, 5, 20);
        LocalDate end = LocalDate.of(2022, 5, 30);
        AvailabilityDTO availabilityDTO = availabilityDTOMock(start, end);
        String key = start + "-" + end;

        when(cacheServiceMock.getCache(key)).thenReturn(availableDatesCache());
        Mono<List<LocalDate>> availabilities = bookingService.availabilities(availabilityDTO);

        StepVerifier
                .create(availabilities)
                .consumeNextWith(dates -> {
                    assertEquals(4, dates.size());
                    assertEquals(dates, availableDates());
                })
                .expectNextCount(0)
                .verifyComplete();

        verify(bookingRepositoryMock, times(0)).availabilities(start,end);
    }

    @Test
    void save() {
        BookingDTO bookingDTO = bookingDTOMock();
        Booking booking = bookingMock();
        Booking savedBooking = bookingWithIdMock();

        assertNull(booking.getId());

        Mockito.doNothing().when(bookingServiceMock).checkAvailability(bookingDTO);
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(savedBooking);

        Booking b = bookingService.save(bookingDTO);

        assertNotNull(b.getId());
        assertEquals(1L, b.getId());

        verify(bookingRepositoryMock, times(1)).save(booking);
    }

    private Set<ConstraintViolation<Object>> validate(Validator validator, BookingDTO dataClass) {
        return validator.validate(dataClass);
    }

    @Test
    void saveWith30DaysInAdvance() {
        BookingDTO bookingDTO = bookingDTOMock();
        Booking booking = bookingMock();
        Booking savedBooking = bookingWithIdMock();

        bookingDTO.setCheckIn(LocalDate.now().plusDays(31));

        assertNull(booking.getId());

        Mockito.doNothing().when(bookingServiceMock).checkAvailability(bookingDTO);
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(savedBooking);

        Set<ConstraintViolation<Object>> violations = validate(validator, bookingDTO);
        assertEquals(1, violations.size());
    }

    @Test
    void saveWithRangeGreaterThan3Days() {
        BookingDTO bookingDTO = bookingDTOMock();
        Booking booking = bookingMock();
        Booking savedBooking = bookingWithIdMock();

        bookingDTO.setCheckOut(bookingDTO.getCheckIn().plusDays(4));

        assertNull(booking.getId());

        Mockito.doNothing().when(bookingServiceMock).checkAvailability(bookingDTO);
        when(bookingMapperMock.toModel(bookingDTO)).thenReturn(booking);
        when(bookingRepositoryMock.save(booking)).thenReturn(savedBooking);

        Set<ConstraintViolation<Object>> violations = validate(validator, bookingDTO);
        assertEquals(1, violations.size());
    }

    @Test
    void update() {
        BookingDTO bookingDTO = bookingDTOMock();
        Booking booking = bookingWithIdMock();

        doNothing().when(bookingServiceMock).checkAvailability(bookingDTO);
        when(bookingRepositoryMock.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepositoryMock.save(booking)).thenReturn(booking);

        Booking b = bookingService.update(1L, bookingDTO);

        assertEquals(1L, b.getId());
        assertEquals(b.getCheckIn(), bookingDTO.getCheckIn());
        assertEquals(b.getCheckOut(), bookingDTO.getCheckOut());

        verify(bookingRepositoryMock, times(1)).save(booking);
    }

    @Test
    void cancel() {
        Booking booking = bookingWithIdMock();
        when(bookingRepositoryMock.findById(1L)).thenReturn(Optional.of(booking));

        assertNotNull(bookingService.findById(1L));
        bookingService.cancel(1L);

        when(bookingRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, ()-> bookingService.findById(1L));
    }

    @Test
    void checkAvailability() {
        BookingDTO bookingDTO = bookingDTOMock();
        doNothing().when(bookingServiceMock).checkAvailability(bookingDTO);
    }

    @Test
    void checkBlockedAvailability() {
        BookingDTO bookingDTO = bookingDTOMock();
        List<Booking> bookings = availabilitiesMock();

        when(bookingRepositoryMock.checkAvailability(bookingDTO.getCheckIn())).thenReturn(bookings);
        assertThrows(ResponseStatusException.class, ()-> bookingService.checkAvailability(bookingDTO));
    }
}