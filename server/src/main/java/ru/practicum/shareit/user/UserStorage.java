    package ru.practicum.shareit.user;

    import lombok.Getter;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Repository;
    import ru.practicum.shareit.user.model.User;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.NoSuchElementException;

    @Slf4j
    @Repository
    public class UserStorage {

        private Long id = 0L;
        private List<User> users = new ArrayList<>();
        @Getter
        private HashMap<Long, User> usersBase = new HashMap<>();

        public void getIdUser(User user) {
            ++id;
            user.setId(id);
        }

        public List<User> getAllUsers() {
            return new ArrayList<>(usersBase.values());
        }

        public User getUserStorage(Long id) {
            if (!usersBase.containsKey(id))
                throw new NoSuchElementException("Такого пользователя нет! getUserStorage()");
            return usersBase.get(id);
        }

        public User deleteUser(Long id) {
            if (!usersBase.containsKey(id))
                throw new NoSuchElementException("Такого пользователя нет! deleteFriendsStorage()");
            User user = usersBase.get(id);
            usersBase.remove(id);
            return  user;
        }

        public User createUser(User user){
            usersBase.put(user.getId(), user);
            return usersBase.get(user.getId());
        }

        public User patchUser(User user) {
            usersBase.put(user.getId(), user);
            return usersBase.get(user.getId());
        }
    }
