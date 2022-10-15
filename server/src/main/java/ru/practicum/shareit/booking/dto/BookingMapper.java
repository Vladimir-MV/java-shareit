    package ru.practicum.shareit.booking.dto;

    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.item.dto.ItemMapper;
    import ru.practicum.shareit.user.dto.UserMapper;
    import java.util.ArrayList;
    import java.util.List;

    public class BookingMapper {
        public static BookingDto toBookingDto(Booking booking) {
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    booking.getStatus(),
                    UserMapper.toUserDto(booking.getBooker()),
                    ItemMapper.toItemDto(booking.getItem())
            );
        }

        public static BookingDtoLastNextOut toBookingDtoLastNextOut(Booking booking) {
            return new BookingDtoLastNextOut(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        }

        public static Booking toBookingDtoIn(BookingDtoIn bookingDto) {
            return new Booking (
                    bookingDto.getStart(),
                    bookingDto.getEnd()
            );
        }

        public static List<BookingDto> toListBookingDto (List<Booking> listBooking) {
            List<BookingDto> listDto = new ArrayList<>();
            for (Booking booking : listBooking) {
                listDto.add(toBookingDto(booking));
            }
            return listDto;
        }
    }
