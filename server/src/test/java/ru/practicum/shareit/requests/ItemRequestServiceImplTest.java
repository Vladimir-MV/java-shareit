    package ru.practicum.shareit.requests;

    import org.junit.jupiter.api.Test;
    import ru.practicum.shareit.booking.Status;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.dto.ItemDto;
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
    class ItemRequestServiceImplTest {
        private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        private UserRepository userRepository = Mockito.mock(UserRepository.class);
        private ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        private ItemRequestService itemRequestService = new ItemRequestServiceImpl(itemRequestRepository,
                userRepository, itemRepository);

        private Item item;
        private ItemDto itemDto;
        private User user;
        private User userNew;
        private UserDto userDto;
        private Booking booking;
        private List<Booking> list;
        private List<Item> listItem;
        private List<ItemRequest> listItemRequest;
        private List<ItemDto> listItemDto;
        private ItemRequest itemRequest;
        private ItemRequestDto itemRequestDto;
        @BeforeEach
        void setUp() {
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
            booking = new Booking(
                    1L,
                    LocalDateTime.of(2022, 10, 30, 10, 10, 10),
                    LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                    item,
                    userNew,
                    Status.REJECTED);

            itemRequest = new ItemRequest(
                    1L,
                    "description",
                    user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
            itemRequestDto = new ItemRequestDto(
                    1L,
                    "description",
                    userDto,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
            list = Arrays.asList(booking);
            listItem = Arrays.asList(item);
            listItemDto = Arrays.asList(itemDto);
            listItemRequest = Arrays.asList(itemRequest);
        }

        @Test
        void whenGetItemRequestDtoSave_thenCreateItemRequestDatabase() throws ValidationException {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
            ItemRequestDto itemRequestDtoTest = itemRequestService.createItemRequest (Optional.of(1L), itemRequestDto);

            Assertions.assertEquals(1L, itemRequestDtoTest.getId());
            Assertions.assertEquals("description", itemRequestDtoTest.getDescription());
            Assertions.assertEquals("John", itemRequestDtoTest.getRequestor().getName());
        }

        @Test
        void whenItemRequestDtoNotDescription_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
            Assertions.assertThrows(ValidationException.class, () -> itemRequestService.createItemRequest (Optional.of(1L),
                    new ItemRequestDto(
                            1L,
                            "",
                            userDto,
                            LocalDateTime.of(2022, 4, 4, 4, 4, 4))));
        }

        @Test
        void whenGeAllItemRequest_thenGetAllItemRequest() throws ValidationException {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(1L)).thenReturn(listItemRequest);
            Mockito
                    .when(itemRepository.findByRequest_IdOrderByCreated(1L)).thenReturn(Optional.of(listItem));
            List<ItemRequestDto> listItemRequestDtoTest = itemRequestService.findAllItemRequest(Optional.of(1L));
            Assertions.assertEquals(1L, listItemRequestDtoTest.get(0).getId());
            Assertions.assertEquals("description", listItemRequestDtoTest.get(0).getDescription());
            Assertions.assertEquals("John", listItemRequestDtoTest.get(0).getRequestor().getName());

    }

        @Test
        void whenGetItemRequestById_thenGetItemRequest() throws ValidationException {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            Mockito
                    .when(itemRepository.findByRequest_IdOrderByCreated(1L)).thenReturn(Optional.of(listItem));
            ItemRequestDto itemRequestDtoTest = itemRequestService.findItemRequestById (Optional.of(1L), Optional.of(1L));
            Assertions.assertEquals(1L, itemRequestDtoTest.getId());
            Assertions.assertEquals("description", itemRequestDtoTest.getDescription());
            Assertions.assertEquals("John", itemRequestDtoTest.getRequestor().getName());
        }
        @Test
        void whenGetItemRequestIdFalse_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            Mockito
                    .when(itemRepository.findByRequest_IdOrderByCreated(1L)).thenReturn(Optional.of(listItem));
            Assertions.assertThrows(NoSuchElementException.class, () -> itemRequestService
                    .findItemRequestById (Optional.of(1L), Optional.empty()));
        }

        @Test
        void whenGetItemRequestItemRequestFalse_thenTrowCustomException() {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            Mockito
                    .when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty());
            Mockito
                    .when(itemRepository.findByRequest_IdOrderByCreated(1L)).thenReturn(Optional.of(listItem));
            Assertions.assertThrows(NoSuchElementException.class, () -> itemRequestService
                    .findItemRequestById (Optional.of(1L), Optional.of(1L)));
        }

        @Test
        void findItemRequestPageable() {
            // протестирован в ItemRequestServiceImplIntegrateTest()
        }
    }