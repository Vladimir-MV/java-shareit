    package ru.practicum.shareit.user;

    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.model.User;
    import java.util.List;
    import java.util.Optional;

    public interface UserService {
        List<User> findAllUserService();

        User findUserByIdService(Optional<Long> id);

        User deleteUserService(Optional<Long> id);

        User createUserService(User user) throws ValidationException, ConflictException;

        User patchUserService(User user, Optional<Long> id) throws ValidationException, ConflictException;

        boolean validationUser(User user) throws ValidationException, ConflictException;
    }
