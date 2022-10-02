    package ru.practicum.shareit.user;

    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;

    import javax.validation.Valid;
    import java.util.List;
    import java.util.Optional;

    public interface UserService {
        List<UserDto> findAllUser();
        UserDto findUserById(Optional<Long> id);
        UserDto deleteUser(Optional<Long> id);
        UserDto createUser(@Valid UserDto user) throws ValidationException, ConflictException;
        UserDto patchUser(UserDto user, Optional<Long> id) throws ValidationException, ConflictException;
        boolean validationUser(User user) throws ValidationException, ConflictException;

       // Object findAllUser(T any);
    }
