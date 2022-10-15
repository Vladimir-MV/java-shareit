    package ru.practicum.shareit.booking;

    import org.junit.jupiter.api.Test;
    import ru.practicum.shareit.booking.dto.BookingDto;
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
        private BookingDto bookingDto;
        private BookingDto bookingDtoIn;
        private List<Booking> list;
        private List<ItemDto> listItem;
        private List<BookingDto> bookingList;
        private BookingDto bookingDtoLastNextOut;
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

//            itemDtoLastNext = new ItemDtoLastNext(
//                    1L,
//                    "john",
//                    "description",
//                    true,
//                    bookingDto,
//                    new BookingDto(
//                            1L,
//                            LocalDateTime.of(2022, 10, 20, 10, 10, 10),
//                            LocalDateTime.of(2022, 11, 11, 11, 11, 11),
//                            Status.REJECTED,
//                            userDto,
//                            itemDto),
//                    Arrays.asList(new CommentDto()),
//                    new ItemRequestDto(
//                            1L,
//                            "description",
//                            userDto,
//                            LocalDateTime.of(2022, 4, 4, 4, 4, 4),
//                            listItem));
            bookingDtoLastNextOut = new BookingDto(
                    3L,
                    1L,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMonths(3));

            itemDtoLastNext = new ItemDtoLastNext(
                    2L,
                    "john",
                    "description",
                    true,
                    bookingDtoLastNextOut,
                    new BookingDto(
                            3L,
                            1L,
                            LocalDateTime.now(),
                            LocalDateTime.now().plusMonths(3)),
                    Arrays.asList(new CommentDto()),
                    new ItemRequestDto(
                            1L,
                            "description",
                            userDto,
                            LocalDateTime.of(2022, 4, 4, 4, 4, 4),
                            listItem));
            booking = new Booking(
                    1L,
                    LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    item,
                    userNew,
                    Status.REJECTED);
            bookingDto = new BookingDto(
                    1L,
                    LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    Status.REJECTED,
                    userDto,
                    itemDto);
            bookingDtoIn = new BookingDto(
                    1L,
                    LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11));
            itemRequest = new ItemRequest(
                    1L,
                    "description",
                    user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
            list = Arrays.asList(booking);
            listItem = Arrays.asList(itemDto);
            bookingList = Arrays.asList(bookingDto);
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
            BookingDto bookingDtoOutTest = bookingService.createBooking (Optional.of(1L), Optional.of(bookingDto));
            Assertions.assertEquals(1L, bookingDtoOutTest.getBooker().getId());
            Assertions.assertEquals("2022-10-20T10:10:10", bookingDtoOutTest.getStart().toString());
            Assertions.assertEquals("2022-11-11T11:11:11", bookingDtoOutTest.getEnd().toString());
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
                    Optional.of(new BookingDto(
                            2L,
                            LocalDateTime.of(2022, 10, 20, 10, 10, 10),
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
                    Optional.of(new BookingDto(
                            1L,
                            LocalDateTime.of(2022, 10, 20, 10, 10, 10),
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
                    Optional.of(new BookingDto(
                            1L,
                            LocalDateTime.of(2022, 10, 20, 10, 10, 10),
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
                    Optional.of(new BookingDto(
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
            BookingDto bookingDtoOutTest = bookingService.patchStatusBooking(Optional.of(2L), Optional.of(1L), true);
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
            List<BookingDto> bookingListTest = bookingService.findBookingsAllById(Optional.of(1L));
            Assertions.assertEquals(1L, bookingListTest.get(0).getId());
            Assertions.assertEquals("2022-10-20T10:10:10", bookingListTest.get(0).getStart().toString());
            Assertions.assertEquals("2022-11-11T11:11:11", bookingListTest.get(0).getEnd().toString());
        }

        @Test
        void whenGetBookingDtoById_thenGetBookingById() throws ValidationException {
            Mockito
                    .when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            BookingDto bookingTest = bookingService.findBookingById(Optional.of(2L), Optional.of(1L));
            Assertions.assertEquals(1L, bookingTest.getId());
            Assertions.assertEquals("2022-10-20T10:10:10", bookingTest.getStart().toString());
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