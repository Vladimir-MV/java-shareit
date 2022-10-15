    package ru.practicum.shareit.booking;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.exception.MessageFailedException;
    import javax.validation.Valid;
    import javax.validation.constraints.Positive;
    import javax.validation.constraints.PositiveOrZero;

    @Controller
    @RequestMapping(path = "/bookings")
    @RequiredArgsConstructor
    @Slf4j
    public class BookingController {
        BookingClient bookingClient;
       @Autowired
        protected BookingController(BookingClient bookingClient) {
            this.bookingClient = bookingClient;
        }

        @GetMapping
        public ResponseEntity<Object> findBookingsGateway(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) throws MessageFailedException {
            State state = State.from(stateParam)
                    .orElseThrow(() -> new MessageFailedException());
            log.info("findBooking, get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
            return bookingClient.findBookingsStateGateway(userId, from, size, state);
        }

        @PostMapping
        public ResponseEntity<Object> createBookingGateway (@Valid @RequestHeader("X-Sharer-User-Id") long userId,
             @RequestBody BookingDto bookingDto) {
            log.info("create booking {}, userId={}", bookingDto, userId);
            return bookingClient.createBookingGateway(userId, bookingDto);
        }
        @PatchMapping("/{bookingId}")
        public ResponseEntity<Object> patchStatusBookingGateway(@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId, @RequestParam(value="approved") Boolean approved) {
            log.info("patchStatusBookingGateway bookingId={}, userId={}, approved {}", bookingId, userId, approved);
            return bookingClient.patchStatusBookingGateway(userId, bookingId, approved);
        }

        @GetMapping("/{bookingId}")
        public ResponseEntity<Object> findBookingIdGateway(@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId) {
            log.info("findBookingIdGateway bookingId={}, userId={}", bookingId, userId);
            return bookingClient.getBookingGateway(userId, bookingId);
        }
        @GetMapping("/")
        public ResponseEntity<Object> findBookingGateway(@RequestHeader("X-Sharer-User-Id") long userId) {
            log.info("findBookingGateway, userId={}", userId);
            return bookingClient.findBookingsAllByIdGateway(userId);
        }
        @GetMapping("/owner")
        public ResponseEntity<Object> findBookingOwnerGateway(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) throws MessageFailedException {
            State state = State.from(stateParam)
                    .orElseThrow(() -> new MessageFailedException());
            log.info("findBookingOwnerGateway, get booking Owner with state {}, userId={}, from={}, size={}", state, userId, from, size);
            return bookingClient.findBookingsOwnerStateGateway(userId, from, size, state);
        }
    }
