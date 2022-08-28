    package ru.practicum.shareit.booking;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.booking.dto.BookingDtoById;
    import ru.practicum.shareit.booking.dto.BookingDtoIn;
    import ru.practicum.shareit.booking.dto.BookingDtoOut;
    import ru.practicum.shareit.exception.MessageFailedException;
    import ru.practicum.shareit.exception.ValidationException;

    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
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
        protected BookingDtoById create(@Valid @RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestBody Optional<BookingDtoIn> bookingDtoIn) throws ValidationException {
            return bookingService.createBooking(idUser, bookingDtoIn);
        }
        @PatchMapping("/{bookingId}")
        protected BookingDtoOut putStatus(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> bookingId, @RequestParam("approved") String approved)
            throws ValidationException {
            return bookingService.patchStatusBooking(idUser, bookingId, approved);
        }
        @GetMapping("/{bookingId}")
        protected BookingDtoOut findBookingId(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable(value = "bookingId", required = false) Optional<Long> bookingId)
            throws ValidationException {
            return bookingService.findBookingById(idUser, bookingId);
        }
        @GetMapping()
        protected List<BookingDtoOut> findBooking(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state)
            throws ValidationException, MessageFailedException {
            return bookingService.findBookingsState(userId, state);
        }
        @GetMapping("/")
        protected List<BookingDtoOut> findBooking(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId)
            throws ValidationException {
            return bookingService.findBookingsAllById(userId);
        }
        @GetMapping("/owner")
        protected List<BookingDtoOut> findBookingOwner(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state)
            throws ValidationException, MessageFailedException {
            return bookingService.findBookingsOwnerState(idUser, state);
        }
    }
