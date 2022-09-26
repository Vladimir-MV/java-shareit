    package ru.practicum.shareit.booking;

    import lombok.RequiredArgsConstructor;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.annotation.DirtiesContext;
    import ru.practicum.shareit.booking.dto.BookingDtoIn;
    import ru.practicum.shareit.booking.dto.BookingDtoOut;
    import ru.practicum.shareit.exception.MessageFailedException;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;
    import java.time.LocalDateTime;
    import java.util.*;
    import static ru.practicum.shareit.booking.Status.WAITING;
    import javax.transaction.Transactional;

    @Transactional
    @ExtendWith(MockitoExtension.class)
    @SpringBootTest(
           // properties = "db.name=test",
            webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    @DirtiesContext
    public class BookingServiceImplBookingOwnerStateIntegrateTest {
        @Autowired
        private BookingRepository bookingRepository;
        @Autowired
        private ItemRepository itemRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private BookingService bookingService = new BookingServiceImpl(bookingRepository, itemRepository,
                userRepository);

        private User user = new User(
                1L,
                "John",
                "john.doe@mail.com");
        private User userNew = new User(
                2L,
                "Jack",
                "jack.doe@mail.com");
        private UserDto userDto = new UserDto(
                1L,
                "John",
                "john.doe@mail.com");
        private ItemRequest itemRequest = new ItemRequest(
                1L,
                "description",
                user,
                LocalDateTime.of(2022, 4, 4, 4, 4, 4));
        private Item item = new Item(
                1L,
                "отвертка",
                "хорошая",
                true,
                userNew,
                itemRequest);
        private ItemDto itemDto = new ItemDto(
                1L,
                "отвертка",
                "хорошая",
                true,
                userDto,
                1L);

        private BookingDtoIn bookingDtoIn = new BookingDtoIn(
                1L,
                LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                LocalDateTime.of(2022, 11, 11, 11, 11, 11));
        private BookingDtoOut bookingDtoOut = new BookingDtoOut(
                1L,
                LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                WAITING,
                userDto,
                itemDto);



        @Test
        void whenGetAllBookingsOwnerStateIntegrateTest_thenGetListBookingsOwner() throws ValidationException, MessageFailedException {
            userRepository.save(user);
            userRepository.save(userNew);
            itemRepository.save(item);
            bookingService.createBooking(Optional.of(1L), Optional.of(bookingDtoIn));
            List<BookingDtoOut> bookingDtoOutListTest = bookingService.findBookingsOwnerState (Optional.of(2L),
                    Optional.of(0), Optional.of(2), "ALL");
            Assertions.assertEquals(1L, bookingDtoOutListTest.get(0).getId());
            Assertions.assertEquals("2022-10-10T10:10:10", bookingDtoOutListTest.get(0).getStart().toString());
            Assertions.assertEquals("2022-11-11T11:11:11", bookingDtoOutListTest.get(0).getEnd().toString());
        }
    }
