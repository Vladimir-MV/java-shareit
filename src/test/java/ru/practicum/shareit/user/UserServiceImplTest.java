    package ru.practicum.shareit.user;

    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.Mockito;
    import org.mockito.junit.jupiter.MockitoExtension;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;
    import java.util.Arrays;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;

    @ExtendWith(MockitoExtension.class)
    class UserServiceImplTest {

        private UserRepository userRepository = Mockito.mock(UserRepository.class);
        private UserService userService = new UserServiceImpl(userRepository);
        private User user;
        private UserDto userDtoBug;
        private UserDto userDto;
        private List<User> list;

        @BeforeEach
        void setUp() {
            user = new User(
                    1L,
                    "John",
                    "john.doe@mail.com");
            userDtoBug = new UserDto(
                    1L,
                    null,
                    "john.doe@mail.com");
            userDto = new UserDto(
                    1L,
                    "John",
                    "john.doe@mail.com");

            list = Arrays.asList(user);
        }

        @Test
        void whenGetAllUsers_thenGetListAllUsers () {
            Mockito
                    .when(userRepository.findAll()).thenReturn(list);
            List<UserDto> listTest = userService.findAllUser();
            Assertions.assertEquals(1L, listTest.get(0).getId());
            Assertions.assertEquals("John", listTest.get(0).getName());
            Assertions.assertEquals("john.doe@mail.com", listTest.get(0).getEmail());
        }

        @Test
        void whenGetUserById_thenGetUser () {
            Mockito
                    .when(userRepository.findById(1l)).thenReturn(Optional.ofNullable(user));
            UserDto userDtoTest = userService.findUserById(Optional.of(1l));
            Assertions.assertEquals(1L, userDtoTest.getId());
            Assertions.assertEquals("John", userDtoTest.getName());
            Assertions.assertEquals("john.doe@mail.com", userDtoTest.getEmail());
        }

        @Test
        void whenGetFalseIdUser_thenTrowCustomException(){
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
            Assertions.assertThrows(NoSuchElementException.class, () -> userService.deleteUser(Optional.of(2L)));
        }

        @Test
        void whenGetIDUserToDelete_thenDeleteUser () {
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
            userService.deleteUser(Optional.of(1L));

            Mockito.verify(userRepository, Mockito.times(1))
                    .delete(user);
        }

        @Test
        void whenGetFalseIDUserDelete_thenTrowCustomException(){
            Mockito
                    .when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
            Assertions.assertThrows(NoSuchElementException.class, () -> userService.deleteUser(Optional.of(2L)));
       }

        @Test
        void whenGetUserDtoSave_thenCreateUserDatabase() throws ValidationException, ConflictException {
            Mockito
                    .when(userRepository.save(user)).thenReturn(user);
            UserDto userDtoTest = userService.createUser(userDto);
            Assertions.assertEquals(1L, userDtoTest.getId());
            Assertions.assertEquals("John", userDtoTest.getName());
            Assertions.assertEquals("john.doe@mail.com", userDtoTest.getEmail());
        }
        @Test
        void whenGetUserDtoBugSave_thenTrowCustomException() {
            Mockito
                    .when(userRepository.save(user)).thenReturn(user);
            Assertions.assertThrows(ValidationException.class, () -> userService.createUser(userDtoBug));
        }

        @Test
        void whenGetUserDtoAndIdPatch_thenSaveUserDatabase() throws ValidationException, ConflictException {
            Mockito
                   .when(userRepository.save(user)).thenReturn(user);
            UserDto userDtoTest = userService.patchUser(userDto, Optional.of(1L));
            Assertions.assertEquals(1L, userDtoTest.getId());
            Assertions.assertEquals("John", userDtoTest.getName());
            Assertions.assertEquals("john.doe@mail.com", userDtoTest.getEmail());
        }
        @Test
        void whenGetUserDtoAndFalseIdPatch_thenTrowCustomException() {
            Mockito
                    .when(userRepository.save(user)).thenReturn(user);
            Assertions.assertThrows(NoSuchElementException.class, () -> userService.patchUser(userDto, Optional.empty()));
        }
    }