    package ru.practicum.shareit.booking;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.exception.MessageFailedException;
    import ru.practicum.shareit.exception.ValidationException;
    import javax.servlet.http.HttpServletRequest;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping(path = "/bookings")
    public class BookingController {

        BookingService bookingService;
        @Autowired
        protected BookingController(BookingService bookingService) {
            this.bookingService = bookingService;
        }

        @RequestMapping(value ="/", produces = "application/json")
        protected String getURLValue(HttpServletRequest request){
            String test = request.getRequestURI();
            return test;
        }

        @PostMapping()
        protected BookingDto create(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestBody Optional<BookingDto> bookingDto) throws ValidationException {
            return bookingService.createBooking(idUser, bookingDto);
        }
        @PatchMapping("/{bookingId}")
        protected BookingDto patchStatus(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> bookingId, @RequestParam(value="approved") Boolean approved)
            throws ValidationException {
            return bookingService.patchStatusBooking(idUser, bookingId, approved);
        }
        @GetMapping("/{bookingId}")
        protected BookingDto findBookingId(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable(value = "bookingId", required = false) Optional<Long> bookingId)
            throws ValidationException {
            return bookingService.findBookingById(idUser, bookingId);
        }
        @GetMapping()
        protected List<BookingDto> findBooking(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId,
            @RequestParam(value = "from") Optional<Integer> from,
            @RequestParam(value = "size") Optional<Integer> size,
            @RequestParam(value = "state") String state)
            throws ValidationException, MessageFailedException {
            return bookingService.findBookingsState(userId, from, size, state);
        }

        @GetMapping("/")
        protected List<BookingDto> findBooking(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId)
            throws ValidationException {
            return bookingService.findBookingsAllById(userId);
        }
        @GetMapping("/owner")
        protected List<BookingDto> findBookingOwner(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestParam(value = "from") Optional<Integer> from,
            @RequestParam(value = "size") Optional<Integer> size,
            @RequestParam(value = "state") String state)
            throws ValidationException, MessageFailedException {
            return bookingService.findBookingsOwnerState(idUser, from, size, state);
        }

    }
