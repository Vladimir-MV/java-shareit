    package ru.practicum.shareit.item;

    import lombok.RequiredArgsConstructor;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.annotation.DirtiesContext;
    import org.springframework.test.annotation.Rollback;
    import ru.practicum.shareit.booking.BookingRepository;
    import ru.practicum.shareit.booking.Status;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import ru.practicum.shareit.item.model.Comment;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.ItemRequestRepository;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;
    import javax.transaction.Transactional;
    import java.time.LocalDateTime;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Optional;

    import static ru.practicum.shareit.booking.Status.WAITING;

    @Transactional
    @ExtendWith(MockitoExtension.class)
    @SpringBootTest(
            webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    @DirtiesContext
    public class ItemServiceImplIntegrateTest {
        @Autowired
        private BookingRepository bookingRepository;
        @Autowired
        private ItemRepository itemRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private CommentRepository commentRepository;
        @Autowired
        private ItemRequestRepository itemRequestRepository;
        @Autowired
        private ItemService itemService = new ItemServiceImpl(itemRepository, userRepository,
                commentRepository, bookingRepository, itemRequestRepository);

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
            private Comment comment = new Comment(
                    1L,
                    "норм",
                    item,
                    user,
                    LocalDateTime.now());
            private CommentDto commentDto = new CommentDto(
                    1L,
                    "норм",
                    itemDto,
                    "John",
                    LocalDateTime.now());
            private Booking booking = new Booking(
                    1L,
                    LocalDateTime.now(),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    item,
                    userNew,
                    Status.REJECTED);
            private BookingDto bookingDtoById = new BookingDto(
                    1L,
                    1L,
                    LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    WAITING,
                    userDto,
                    itemDto);
            private BookingDto bookingDtoLastNextOut = new BookingDto(
                    1L,
                    1L,
                    LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    WAITING,
                    userDto,
                    itemDto);

            private BookingDto bookingDto = new BookingDto(
                    1L,
                    1L,
                    LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    WAITING,
                    userDto,
                    itemDto);

            private List<Booking> list = Arrays.asList(booking);
            private List<Item> listItem = Arrays.asList(item);
            private List<ItemDto> listItemDto = Arrays.asList(itemDto);
            private List<CommentDto> listCommentDto = Arrays.asList(commentDto);

            private ItemDtoLastNext itemDtoLastNext = new ItemDtoLastNext(
                    1L,
                    "john",
                    "description",
                    true,
                    bookingDtoLastNextOut,
                    new BookingDto(
                            1L,
                            1L,
                            LocalDateTime.of(2022, 10, 30, 10, 10, 10),
                            LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                            WAITING,
                            userDto,
                            itemDto),
                    Arrays.asList(new CommentDto()),
                    new ItemRequestDto(
                            1L,
                            "description",
                            userDto,
                            LocalDateTime.of(2022, 4, 4, 4, 4, 4),
                            listItemDto));

        @Rollback(false)
        @Test
        void whenGetItemSearchTextIntegrateTest_thenGetItem() throws ValidationException {
            userRepository.save(user);
            userRepository.save(userNew);
            itemRequestRepository.save(itemRequest);
            itemRepository.save(item);
            List<ItemDto> itemDtoListTest = itemService.findItemSearch(Optional.of(2L), "отвертка",
                    Optional.of(0), Optional.of(1));
            Assertions.assertEquals(1L, itemDtoListTest.get(0).getId());
            Assertions.assertEquals("хорошая", itemDtoListTest.get(0).getDescription());
            Assertions.assertEquals("отвертка", itemDtoListTest.get(0).getName());
        }

        @Test
        void whenGetAllItemsOwnerIntegrateTest_thenGetAllItemsOwner() throws ValidationException {
            List<ItemDtoLastNext> itemDtoLastNextTest = itemService.findAllItemsOwner (Optional.of(2L), Optional.of(0), Optional.of(2));
            Assertions.assertEquals(1L, itemDtoLastNextTest.get(0).getId());
            Assertions.assertEquals("хорошая", itemDtoLastNextTest.get(0).getDescription());
            Assertions.assertEquals("отвертка", itemDtoLastNextTest.get(0).getName());
        }
    }
