    package ru.practicum.shareit.item;

    import org.junit.jupiter.api.Test;
    import ru.practicum.shareit.booking.BookingRepository;
    import ru.practicum.shareit.booking.Status;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import ru.practicum.shareit.item.model.Comment;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.ItemRequestRepository;
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
    public class ItemServiceImplTest {
        private BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        private UserRepository userRepository = Mockito.mock(UserRepository.class);
        private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        private ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        private ItemService itemService = new ItemServiceImpl(itemRepository, userRepository,
                commentRepository, bookingRepository, itemRequestRepository);

        private Item item;
        private ItemDto itemDto;
        private CommentDto commentDto;
        private Comment comment;
        private ItemDtoLastNext itemDtoLastNext;
        private User user;
        private User userNew;
        private UserDto userDto;
        private Booking booking;
        private BookingDto bookingDtoById;
        private BookingDto bookingDtoLastNextOut;
        private BookingDto bookingDto;
        private List<Booking> list;
        private List<CommentDto> listCommentDto;
        private List<Item> listItem;
        private List<ItemDto> listItemDto;
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
            comment = new Comment(
                    1L,
                    "норм",
                    item,
                    user,
                    LocalDateTime.now());
            commentDto = new CommentDto(
                    1L,
                    "норм",
                    itemDto,
                    "John",
                    LocalDateTime.now());

            itemDtoLastNext = new ItemDtoLastNext(
                    1L,
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
                            listItemDto));
            booking = new Booking(
                    1L,
                    LocalDateTime.now(),
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
//            bookingDto = new BookingDto(
//                    1L,
//                    LocalDateTime.of(2022, 10, 10, 20, 10, 10),
//                    LocalDateTime.of(2022, 11, 11, 11, 11, 11));

            itemRequest = new ItemRequest(
                    1L,
                    "description",
                    user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
            list = Arrays.asList(booking);
            listItem = Arrays.asList(item);
            listItemDto = Arrays.asList(itemDto);
            listCommentDto = Arrays.asList(commentDto);
            state = "state";
            from = 1;
            size = 4;
        }

        @Test
        void whenGetItemDtoSave_thenCreateUserDatabase() throws ValidationException {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);
            ItemDto itemDtoTest = itemService.createItem (Optional.of(1L), itemDto);

            Assertions.assertEquals(1L, itemDtoTest.getId());
            Assertions.assertEquals("отвертка", itemDtoTest.getName());
            Assertions.assertEquals("John", itemDtoTest.getOwner().getName());
        }

        @Test
        void whenGetItemDtoNotNameSave_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);
            Assertions.assertThrows(ValidationException.class, () -> itemService.createItem(Optional.of(1L),
                    new ItemDto(
                    1L,
                    null,
                    "хорошая",
                    true,
                    userDto,
                    1L)));
        }

        @Test
        void whenGetItemDtoNotDescriptionSave_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);
            Assertions.assertThrows(ValidationException.class, () -> itemService.createItem(Optional.of(1L),
                    new ItemDto(
                            1L,
                            "отвертка",
                            null,
                            true,
                            userDto,
                            1L)));
        }

        @Test
        void whenGetItemDtoNotAvailableSave_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);
            Assertions.assertThrows(ValidationException.class, () -> itemService.createItem(Optional.of(1L),
                    new ItemDto(
                            1L,
                            "отвертка",
                            "хорошая",
                            null,
                            userDto,
                            1L)));
        }

        @Test
        void whenGetItemById_thenGetItem () throws ValidationException {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(commentRepository.findByItem_Id(1L)).thenReturn(Arrays.asList(new Comment(
                            1L,
                            "норм",
                            item,
                            user,
                            LocalDateTime.now())));
            Mockito
                    .when(bookingRepository.findByItem_Id(1L)).thenReturn(Optional.of(list));
            ItemDtoLastNext itemTest = itemService.findItemById(Optional.of(1L), Optional.of(1L));
            Assertions.assertEquals(1L, itemTest.getId());
            Assertions.assertEquals("отвертка", itemTest.getName());
            Assertions.assertEquals("хорошая", itemTest.getDescription());
            Assertions.assertEquals(1L, itemTest.getComments().get(0).getId());

            ItemDtoLastNext itemTest2 = itemService.findItemById(Optional.of(2L), Optional.of(1L));
            Assertions.assertEquals(1L, itemTest2.getId());
            Assertions.assertEquals("отвертка", itemTest2.getName());
            Assertions.assertEquals("хорошая", itemTest2.getDescription());
        }

        @Test
        void whenGetItemNotIdSave_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Assertions.assertThrows(NoSuchElementException.class, () -> itemService.findItemById(Optional.of(1L), Optional.empty()));
        }

        @Test
        void whenPatchItem_thenGetItemDto () throws ValidationException {
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);

            ItemDto itemTest = itemService.patchItem(Optional.of(2L), itemDto, Optional.of(1L));
            Assertions.assertEquals(1L, itemTest.getId());
            Assertions.assertEquals("отвертка", itemTest.getName());
            Assertions.assertEquals("хорошая", itemTest.getDescription());
        }
        @Test
        void whenPatchItemNotId_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);
            Assertions.assertThrows(NoSuchElementException.class, () -> itemService.patchItem(Optional.of(2L),
                    itemDto, Optional.empty()));
        }

        @Test
        void whenPatchItemNotOwner_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(itemRepository.save(item)).thenReturn(item);
            Assertions.assertThrows(NoSuchElementException.class, () -> itemService.patchItem(Optional.of(1L),
                    itemDto, Optional.of(1L)));
        }


        @Test
        void whenDeleteItem_thenDeleteItemAndGetItemDto () throws ValidationException {
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));

            itemService.deleteItem(Optional.of(2L), Optional.of(1L));

            Mockito.verify(itemRepository, Mockito.times(1))
                    .delete(item);
        }

        @Test
        void whenDeleteItemNotId_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Assertions.assertThrows(NoSuchElementException.class, () -> itemService.deleteItem(Optional.of(2L),
                     Optional.empty()));
        }

        @Test
        void whenDeleteItemNotOwner_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(2L)).thenReturn(Optional.of(userNew));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Assertions.assertThrows(NoSuchElementException.class, () -> itemService.deleteItem(Optional.of(1L),
                    Optional.of(1L)));
        }

        @Test
        void whenCreateComment_thenSaveCommentDto () throws ValidationException {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(bookingRepository.findByItem_IdAndBooker_id(1L, 1L)).thenReturn(Optional.of(list));
            Mockito
                    .when(commentRepository.save(comment)).thenReturn(comment);
            CommentDto commentDtoTest = itemService.createComment(Optional.of(1L), Optional.of(1L), commentDto);
            Assertions.assertEquals(1L, commentDtoTest.getId());
            Assertions.assertEquals("отвертка", commentDtoTest.getItem().getName());
            Assertions.assertEquals("норм", commentDtoTest.getText());
        }

        @Test
        void whenCreateCommentNotText_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(bookingRepository.findByItem_IdAndBooker_id(1L, 1L)).thenReturn(Optional.of(list));
            Mockito
                    .when(commentRepository.save(comment)).thenReturn(comment);
            Assertions.assertThrows(ValidationException.class, () -> itemService.createComment(Optional.of(1L),
                    Optional.of(1L), new CommentDto(
                            1L,
                            "",
                            itemDto,
                            "John",
                            LocalDateTime.now())));
        }
        @Test
        void whenCreateCommentBookingStartIsAfter_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(bookingRepository.findByItem_IdAndBooker_id(1L, 1L)).thenReturn(Optional.of(Arrays.asList(new Booking(
                            1L,
                            LocalDateTime.of(2022, 10, 20, 10, 10, 10),
                            LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                            item,
                            userNew,
                            Status.REJECTED))));
            Mockito
                    .when(commentRepository.save(comment)).thenReturn(comment);
            Assertions.assertThrows(ValidationException.class, () -> itemService.createComment(Optional.of(1L),
                    Optional.of(1L), commentDto));
        }
        @Test
        void whenCreateCommentBookingEmpty_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
            Mockito
                    .when(bookingRepository.findByItem_IdAndBooker_id(1L, 1L)).thenReturn(Optional.empty());
            Mockito
                    .when(commentRepository.save(comment)).thenReturn(comment);
            Assertions.assertThrows(ValidationException.class, () -> itemService.createComment(Optional.of(1L),
                    Optional.of(1L), commentDto));
        }
        @Test
        void findItemSearch() {
            // тестируется в ItemServiceImplIntegrateTest
        }
    }