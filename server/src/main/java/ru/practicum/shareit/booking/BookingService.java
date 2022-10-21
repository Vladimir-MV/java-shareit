    package ru.practicum.shareit.booking;

    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.exception.MessageFailedException;
    import ru.practicum.shareit.exception.ValidationException;
    import java.util.List;
    import java.util.Optional;

    public interface BookingService {
        BookingDto createBooking(Optional<Long> idUser, Optional<BookingDto> bookingDto)
                throws ValidationException;

        BookingDto patchStatusBooking(Optional<Long> idUser, Optional<Long> approved, Boolean bookingId) throws ValidationException;

        BookingDto findBookingById(Optional<Long> idUser, Optional<Long> bookingId) throws ValidationException;

        List<BookingDto> findBookingsState(Optional<Long> idUser, Optional<Integer> from, Optional<Integer> size, String state)
                throws ValidationException, MessageFailedException;

        List<BookingDto> findBookingsOwnerState(Optional<Long> idUser, Optional<Integer> from, Optional<Integer> size, String state)
                throws ValidationException, MessageFailedException;

        List<BookingDto> findBookingsAllById(Optional<Long> userId) throws ValidationException;


    }
