    package ru.practicum.shareit.requests;

    import lombok.RequiredArgsConstructor;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.annotation.DirtiesContext;
    import org.springframework.test.annotation.Rollback;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;
    import java.time.LocalDateTime;
    import java.util.*;
    import javax.transaction.Transactional;

    @Transactional
    @ExtendWith(MockitoExtension.class)
    @SpringBootTest(
            webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    @DirtiesContext
    public class ItemRequestServiceImplIntegrateTest {
        @Autowired
        private ItemRepository itemRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ItemRequestRepository itemRequestRepository;
        @Autowired
        private ItemRequestService itemRequestService = new ItemRequestServiceImpl(itemRequestRepository,
                userRepository, itemRepository);

        User user = new User(
                    1L,
                    "John",
                    "john.doe@mail.com");
        User userNew = new User(
                    2L,
                    "Jack",
                    "jack.doe@mail.com");
        UserDto userDto = new UserDto(
                    1L,
                    "John",
                    "john.doe@mail.com");
        ItemRequest itemRequest = new ItemRequest(
                1L,
                "description",
                user,
                LocalDateTime.of(2022, 4, 4, 4, 4, 4));
        Item item = new Item(
                    1L,
                    "отвертка",
                    "хорошая",
                    true,
                    userNew,
                    itemRequest);

        ItemRequestDto itemRequestDto = new ItemRequestDto(
                    1L,
                    "description",
                    userDto,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
        @Rollback(false)
        @Test
        void whenGetItemRequestDtoSaveIntegrateTest_thenCreateItemRequestDatabase() throws ValidationException {
            userRepository.save(user);
            ItemRequestDto itemRequestDtoTest = itemRequestService.createItemRequest (Optional.of(1L), itemRequestDto);
            Assertions.assertEquals(1L, itemRequestDtoTest.getId());
            Assertions.assertEquals("description", itemRequestDtoTest.getDescription());
            Assertions.assertEquals("John", itemRequestDtoTest.getRequestor().getName());
        }
        @Test
        void whenGetItemRequestPageableIntegrateTest_thenCreateItemRequestDtoList() throws ValidationException {
            userRepository.save(userNew);
            itemRepository.save(item);
            List<ItemRequestDto> itemRequestDtoListTest = itemRequestService.findItemRequestPageable (Optional.of(2L),
                    Optional.of(0), Optional.of(1));
            Assertions.assertEquals(1L, itemRequestDtoListTest.get(0).getId());
            Assertions.assertEquals("description", itemRequestDtoListTest.get(0).getDescription());
            Assertions.assertEquals("John", itemRequestDtoListTest.get(0).getRequestor().getName());
        }
    }
