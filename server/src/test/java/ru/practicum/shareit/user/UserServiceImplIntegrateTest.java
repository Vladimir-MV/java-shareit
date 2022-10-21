    package ru.practicum.shareit.user;

    import lombok.RequiredArgsConstructor;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.annotation.DirtiesContext;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.util.*;
    import javax.transaction.Transactional;

    @Transactional
    @ExtendWith(MockitoExtension.class)
    @SpringBootTest(
            webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    @DirtiesContext
    public class UserServiceImplIntegrateTest {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private UserService userService = new UserServiceImpl(userRepository);
            UserDto userDto = new UserDto(
                    1L,
                    "Jack",
                    "john.doe@mail.com");
        UserDto userDtoNew = new UserDto(
                null,
                "John",
                "john.doe@mail.com");

        @Test
        void whenCreateUserSaveIntegrateTest_thenCreateUserDatabase() throws ValidationException, ConflictException {
            userService.createUser(userDto);
            UserDto userDtoTest = userService.findUserById(Optional.of(1L));
            Assertions.assertEquals(1L, userDtoTest.getId());
            Assertions.assertEquals("Jack", userDtoTest.getName());
        }

        @Test
        void whenPatchUserNameIntegrateTest_thenSaveNewNameUserDatabase() throws ValidationException, ConflictException {
            UserDto userDtoTest2 = userService.patchUser(userDtoNew, Optional.of(1L));
            Assertions.assertEquals(1L, userDtoTest2.getId());
            Assertions.assertEquals("John", userDtoTest2.getName());
        }
    }
