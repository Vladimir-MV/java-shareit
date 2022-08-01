    package ru.practicum.shareit.user;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.dto.UserMapper;
    import ru.practicum.shareit.user.model.User;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;
    @Slf4j
    @Service
    public class UserServiceImpl implements UserService {

        private UserStorage userStorage;

        @Autowired
        public UserServiceImpl(UserStorage userStorage) { // InMemoryFilmStorage filmStorage
            this.userStorage = userStorage;
        }

        private void getIdUserService(User user){
            userStorage.getIdUserStorage(user);
        }

        public List<UserDto> findAllUserService() {
            List<User> list = userStorage.getAllUsersStorage();
            log.info("Текущее количество пользователей в списке: {}", list.size());
            List<UserDto> listDto = new ArrayList<>();
            for (User user: list){
                listDto.add(UserMapper.toUserDto(user));
            }
            return listDto;
        }

        public UserDto findUserByIdService(Optional<Long> id) {
            if (id.isPresent()) {
                User user = userStorage.getUserStorage(id.get());
                log.info("Пользователь по id запросу: {}", user.getName());
                return UserMapper.toUserDto(user);
            }
            throw new NoSuchElementException("Не правильно задан id пользователя! findUserByIdService()");
        }

        public UserDto deleteUserService(Optional<Long> id){
            if (id.isPresent()) {
                User user = userStorage.deleteStorage(id.get());
                log.info("Пользователь: {} удален.", user);
                return UserMapper.toUserDto(user);
            }
            throw new NoSuchElementException("Переменные пути указаны не верно! deleteUserService()");
        }

        public UserDto createUserService(UserDto userDto) throws ValidationException, ConflictException {
            User user = UserMapper.toUser(userDto);
            if (validationUser(user)) {
                User userInStorage = userStorage.createUserStorage(user);
                log.info("Добавлен пользователь: {}", userInStorage.getName());
                return UserMapper.toUserDto(userInStorage);
            }
           throw new ValidationException("Пользователь на создан! createUserService()");
        }

        public UserDto patchUserService(UserDto userDto, Optional<Long> id) throws ValidationException, ConflictException {
            User user = UserMapper.toUser(userDto);
            if (!id.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! patchUserService()");
            user.setId(id.get());
            if (validationUser(user)) {
                if (!(user.getName() == null || user.getName() == ""))
                    userStorage.getUsersBase().get(id.get()).setName(user.getName());
                if (!(user.getEmail() == null || user.getEmail() == ""))
                    userStorage.getUsersBase().get(id.get()).setEmail(user.getEmail());
                User userInStorage = userStorage.getUsersBase().get(id.get());
                log.info("Данные пользователя: {} изменены.", userInStorage.getName());
                return UserMapper.toUserDto(userInStorage);
            }
            throw new ValidationException("Информация о пользователе не обновлена! patchUserService()");
        }

        public boolean validationUser(User user) throws ValidationException, ConflictException {
            if (user.getName() != null && user.getName() != "") {
                for (char ch : user.getName().toCharArray()) {
                    if (ch == ' ') {
                        throw new ValidationException("Имя не может содержать символ пробела! validationUser()");
                    }
                }
            }
            for (User userLook: userStorage.getUsersBase().values()) {
                if (userLook.getEmail().equals(user.getEmail()))
                    throw new ConflictException("Адрес почты уже занят! validationUser()");
            }
            if (user.getId() == null) {
                getIdUserService(user);
            }
            else if (user.getId() <= 0)  {
                throw new NoSuchElementException ("id не может быть отрицательным! validationUser()");
            }
            return true;
        }
    }
