    package ru.practicum.shareit.booking;

    import org.junit.jupiter.api.Test;
    import ru.practicum.shareit.booking.dto.BookingDtoById;
    import ru.practicum.shareit.booking.dto.BookingDtoIn;
    import ru.practicum.shareit.booking.dto.BookingDtoOut;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.UserRepository;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.Mockito;
    import org.mockito.junit.jupiter.MockitoExtension;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;
    import java.time.LocalDateTime;
    import java.util.*;
    import static ru.practicum.shareit.booking.Status.WAITING;

    @ExtendWith(MockitoExtension.class)
    class BookingServiceImplTest {
        private BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        private UserRepository userRepository = Mockito.mock(UserRepository.class);

        private BookingService bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        private Item item;
        private ItemDto itemDto;
        private CommentDto commentDto;
        private ItemDtoLastNext itemDtoLastNext;
        private User user;
        private User userNew;
        private UserDto userDto;
        private Booking booking;
        private BookingDtoById bookingDtoById;
        private BookingDtoOut bookingDtoOut;
        private BookingDtoIn bookingDtoIn;
        private List<Booking> list;
        private List<ItemDto> listItem;
        private List<BookingDtoOut> bookingList;
        private ItemRequest itemRequest;
        String state;
        Integer from;
        Integer size;
        @BeforeEach
        void setUp() throws ValidationException {
            user = new User(
                    1L,
                    "John",
                    "john.doe@mail.com");
            userNew = new User(
                    2L,
                    "Jack",
                    "jack.doe@mail.com");
            userDto = new UserDto(
                    1L,
                    "John",
                    "john.doe@mail.com");
            item = new Item(
                    1L,
                    "отвертка",
                    "хорошая",
                    true,
                    userNew,
                    itemRequest);
            itemDto = new ItemDto(
                    1L,
                    "отвертка",
                    "хорошая",
                    true,
                    userDto,
                    1L);
            commentDto = new CommentDto(
                    1L,
                    "норм",
                    itemDto,
                    "john",
                    LocalDateTime.now());

            itemDtoLastNext = new ItemDtoLastNext(
                    1L,
                    "john",
                    "description",
                    true,
                    bookingDtoById,
                    new BookingDtoById(
                            1L,
                            1L,
                            LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                            LocalDateTime.of(2022, 11, 11, 11, 11, 11)),
                    Arrays.asList(new CommentDto()),
                    new ItemRequestDto(
                            1L,
                            "description",
                            userDto,
                            LocalDateTime.of(2022, 4, 4, 4, 4, 4),
                            listItem));
            booking = new Booking(
                    1L,
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    item,
                    userNew,
                    Status.REJECTED);
            bookingDtoById = new BookingDtoById(
                    1L,
                    1L,
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11));
            bookingDtoIn = new BookingDtoIn(
                    1L,
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11));
            bookingDtoOut = new BookingDtoOut(
                    1L,
                    LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    WAITING,
                    userDto,
                    itemDto);
            itemRequest = new ItemRequest(
                    1L,
                    "description",
                    user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
            list = Arrays.asList(booking);
            listItem = Arrays.asList(itemDto);
            bookingList = Arrays.asList(bookingDtoOut);
            state = "state";
            from = 1;
            size = 4;
        }

        @Test
        void whenGetUserBookingDtoInSave_thenCreateBookingDatabase() throws ValidationException {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            BookingDtoById bookingDtoByIdTest = bookingService.createBooking (Optional.of(1L), Optional.of(bookingDtoIn));
            Assertions.assertEquals(1L, bookingDtoByIdTest.getBookerId());
            Assertions.assertEquals("2022-10-10T10:10:10", bookingDtoByIdTest.getStart().toString());
            Assertions.assertEquals("2022-11-11T11:11:11", bookingDtoByIdTest.getEnd().toString());
        }
        @Test
        void whenGetOwnerBookingDtoInSave_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            Assertions.assertThrows(NoSuchElementException.class,
                    () -> bookingService.createBooking (Optional.of(2L), Optional.of(bookingDtoIn)));
        }
        @Test
        void whenFalseValidationUserBookingDtoInSave_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            Assertions.assertThrows(NoSuchElementException.class,
                    () -> bookingService.createBooking (Optional.of(1L), Optional.of(bookingDtoIn)));
        }
        @Test
        void whenFalseValidationItemBookingDtoInSave_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
            Assertions.assertThrows(NoSuchElementException.class,
                    () -> bookingService.createBooking (Optional.of(1L), Optional.of(bookingDtoIn)));
        }
        @Test
        void whenGetBookingDtoItemAvailableFalse_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(2L)).thenReturn(Optional.of(new Item(
                            2L,
                            "отвертка",
                            "хорошая",
                            false,
                            userNew,
                            itemRequest)));
            Assertions.assertThrows(ValidationException.class, () -> bookingService.createBooking (Optional.of(1L),
                    Optional.of(new BookingDtoIn(
                            2L,
                            LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                            LocalDateTime.of(2022, 11, 11, 11, 11, 11)))));
        }
        @Test
        void whenGetBookingDtoEndBeforeStart_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            Assertions.assertThrows(ValidationException.class, () -> bookingService.createBooking (Optional.of(1L),
                    Optional.of(new BookingDtoIn(
                            1L,
                            LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                            LocalDateTime.of(2022, 9, 11, 11, 11, 11)))));
        }
        @Test
        void whenGetBookingDtoEndBeforeTimeToday_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            LocalDateTime timeToday = LocalDateTime.now();
            Assertions.assertThrows(ValidationException.class, () -> bookingService.createBooking (Optional.of(1L),
                    Optional.of(new BookingDtoIn(
                            1L,
                            LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                            timeToday.minusMonths(1)))));
        }
        @Test
        void whenGetBookingDtoStartBeforeTimeToday_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            LocalDateTime timeToday = LocalDateTime.now();
            Assertions.assertThrows(ValidationException.class, () -> bookingService.createBooking (Optional.of(1L),
                    Optional.of(new BookingDtoIn(
                            1L,
                            timeToday.minusMonths(1),
                            LocalDateTime.of(2022, 9, 11, 11, 11, 11)))));
        }

        @Test
        void whenUserPatchStatusBooking_thenSaveNewBookingStatus() throws ValidationException {
            Mockito
                    .when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            BookingDtoOut bookingDtoOutTest = bookingService.patchStatusBooking(Optional.of(2L), Optional.of(1L), true);
            Assertions.assertEquals(1L, bookingDtoOutTest.getId());
            Assertions.assertEquals(Status.APPROVED, bookingDtoOutTest.getStatus());
        }
        @Test
        void whenFalseValidationBookingInPatch_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito
                    .when(bookingRepository.save(booking)).thenReturn(booking);
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            Assertions.assertThrows(NoSuchElementException.class,
                    () -> bookingService.patchStatusBooking(Optional.of(2L), Optional.of(2L), true));
        }

        @Test
        void whenGetAllBookingDtoById_thenGetAllBookingsListById() throws ValidationException {
            Mockito
                    .when(bookingRepository.findByBooker_IdOrderByStartDesc(1L)).thenReturn(list);
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            List<BookingDtoOut> bookingListTest = bookingService.findBookingsAllById(Optional.of(1L));
            Assertions.assertEquals(1L, bookingListTest.get(0).getId());
            Assertions.assertEquals("2022-10-10T10:10:10", bookingListTest.get(0).getStart().toString());
            Assertions.assertEquals("2022-11-11T11:11:11", bookingListTest.get(0).getEnd().toString());
        }

        @Test
        void whenGetBookingDtoById_thenGetBookingById() throws ValidationException {
            Mockito
                    .when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            BookingDtoOut bookingTest = bookingService.findBookingById(Optional.of(2L), Optional.of(1L));
            Assertions.assertEquals(1L, bookingTest.getId());
            Assertions.assertEquals("2022-10-10T10:10:10", bookingTest.getStart().toString());
            Assertions.assertEquals("2022-11-11T11:11:11", bookingTest.getEnd().toString());
        }

        @Test
        void whenGetBookingDtoByIdNotAccess_thenTrowCustomException() {
            Mockito
                    .when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking));
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Assertions.assertThrows(NoSuchElementException.class,
                    () -> bookingService.findBookingById(Optional.of(1L), Optional.of(1L)));
        }
    }