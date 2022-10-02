    package ru.practicum.shareit.booking;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import ru.practicum.shareit.booking.dto.BookingDtoById;
    import ru.practicum.shareit.booking.dto.BookingDtoIn;
    import ru.practicum.shareit.booking.dto.BookingDtoOut;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.time.LocalDateTime;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.json.JsonTest;
    import org.springframework.boot.test.json.JacksonTester;
    import org.springframework.boot.test.json.JsonContent;
    import static org.assertj.core.api.Assertions.assertThat;
    import static ru.practicum.shareit.booking.Status.APPROVED;

    @JsonTest
    public class BookingDtoJsonTest {
        @Autowired
        private JacksonTester<BookingDtoOut> jsonDto;
        @Autowired
        private JacksonTester<BookingDtoById> jsonDtoById;
        @Autowired
        private JacksonTester<BookingDtoIn> jsonDtoIn;

        BookingDtoOut bookingDtoOut;
        BookingDtoById bookingDtoById;
        BookingDtoIn bookingDtoIn;
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 4, 5, 7);
        LocalDateTime end = LocalDateTime.of(2022, 2, 3, 4, 5, 9);

        @BeforeEach
        void setUp() {
            bookingDtoOut = new BookingDtoOut(
                    6L,
                    start,
                    end,
                    APPROVED,
                    new UserDto(),
                    new ItemDto());

            bookingDtoById = new BookingDtoById(
                    7L,
                    8L,
                    start,
                    end);
            bookingDtoIn = new BookingDtoIn(
                    9L,
                    start,
                    end);
        }

        @Test
        void tesBookingDtoOut() throws Exception {
            JsonContent<BookingDtoOut> result = jsonDto.write(bookingDtoOut);
            assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(6);
            assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-02-03T04:05:07");
            assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-02-03T04:05:09");
            assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
            assertThat(result).doesNotHaveEmptyJsonPathValue("$.booker");
            assertThat(result).doesNotHaveEmptyJsonPathValue("$.item");
        }

        @Test
        void testBookingDtoById() throws Exception {
            JsonContent<BookingDtoById> result2 = jsonDtoById.write(bookingDtoById);
            assertThat(result2).extractingJsonPathNumberValue("$.id").isEqualTo(7);
            assertThat(result2).extractingJsonPathNumberValue("$.bookerId").isEqualTo(8);
            assertThat(result2).extractingJsonPathStringValue("$.start").isEqualTo("2022-02-03T04:05:07");
            assertThat(result2).extractingJsonPathStringValue("$.end").isEqualTo("2022-02-03T04:05:09");
        }

        @Test
        void testBookingDtoIn() throws Exception {
            JsonContent<BookingDtoIn> result3 = jsonDtoIn.write(bookingDtoIn);
            assertThat(result3).extractingJsonPathNumberValue("$.itemId").isEqualTo(9);
            assertThat(result3).extractingJsonPathStringValue("$.start").isEqualTo("2022-02-03T04:05:07");
            assertThat(result3).extractingJsonPathStringValue("$.end").isEqualTo("2022-02-03T04:05:09");
        }
    }
