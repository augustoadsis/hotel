package com.augusto.hotel.booking;

import com.augusto.hotel.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private CacheService cacheService;

    public Mono<List<LocalDate>> availabilities(AvailabilityDTO availabilityDTO) {
        LocalDate start = nonNull(availabilityDTO.getCheckIn()) ? availabilityDTO.getCheckIn() : LocalDate.now();
        LocalDate end = nonNull(availabilityDTO.getCheckOut()) ? availabilityDTO.getCheckOut() : LocalDate.now().plusDays(30);

        if(DAYS.between(LocalDate.now(), end) > 30)
            end = LocalDate.now().plusDays(30);

        List<LocalDate> dates = start.datesUntil(end).collect(Collectors.toList());

        String key = start + "-" + end;
        Mono<List<LocalDate>> cache = cacheService.getCache(key);
        if (cache != null)
            return cache;

        Flux<Booking> availabilities = Flux.fromIterable(bookingRepository.availabilities(start, end));
        return availabilities.doOnNext(a -> {
                    List<LocalDate> blockedDates = a.getCheckIn().datesUntil(a.getCheckOut().plusDays(1)).collect(Collectors.toList());
                    log.info("blocked dates: " + blockedDates);
                    dates.removeAll(blockedDates);
                })
                .last(new Booking())
                .flatMap(n -> Mono.justOrEmpty(dates.stream().filter(d -> d.isAfter(LocalDate.now())).collect(Collectors.toList())))
                .doFinally(n -> cacheService.putCache(key, dates))
                .log();
    }

    public Booking save(BookingDTO bookingDTO) {
        //If we have security implemented we could get user by jwt token, in this case it was passed manually by the DTO
        checkAvailability(bookingDTO);
        Booking booking = bookingMapper.toModel(bookingDTO);
        Booking b = bookingRepository.save(booking);
        cacheService.evictCache();
        return b;
    }

    public Booking update(Long id, BookingDTO bookingDTO) {
        //If we have had implemented security, we could get user by jwt token, in this case it was passed manually by the DTO
        checkAvailability(bookingDTO);
        //We've also could check if the booking is owned by requester
        return bookingRepository.findById(id)
                .map(b -> {
                            b.setCheckIn(bookingDTO.getCheckIn());
                            b.setCheckOut(bookingDTO.getCheckOut());
                            Booking booking = bookingRepository.save(b);
                            cacheService.evictCache();
                            return booking;
                        }).orElseThrow(() -> {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested booking does not exist.");
                        });
    }

    public void cancel(Long id) {
        //If we have had implemented security, we could get user by jwt token, in this case it was passed manually by the DTO
        //We've also could check if the booking is owned by requester
        bookingRepository.deleteById(id);
        cacheService.evictCache();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested booking does not exist.");
                });
    }

    public void checkAvailability(BookingDTO bookingDTO) {
        if (!isEmpty(bookingRepository.checkAvailability(bookingDTO.getCheckIn())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is not available, please try another one.");
    }
}
