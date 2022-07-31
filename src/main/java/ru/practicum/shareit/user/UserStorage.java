    package ru.practicum.shareit.user;

    import lombok.Getter;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Repository;
    import ru.practicum.shareit.user.model.User;

    import java.util.*;

    @Slf4j
    @Repository
    public class UserStorage {

        private Long id = 0L;
        private List<User> users = new ArrayList<>();
        @Getter
        private HashMap<Long, User> usersBase = new HashMap<>();

        public void getIdUserStorage(User user) {
            ++id;
            user.setId(id);
        }

        public List<User> getAllUsersStorage() {
            return new ArrayList<>(usersBase.values());
        }

        public User getUserStorage(Long id) {
            if (!usersBase.containsKey(id))
                throw new NoSuchElementException("Такого пользователя нет! getUserStorage()");
            return usersBase.get(id);
        }

        public User deleteStorage(Long id) {
            if (!usersBase.containsKey(id))
                throw new NoSuchElementException("Такого пользователя нет! deleteFriendsStorage()");
            User user = usersBase.get(id);
            usersBase.remove(id);
            return  user;
        }

        public User createUserStorage(User user){
            usersBase.put(user.getId(), user);
            return usersBase.get(user.getId());
        }

        public User patchUserStorage(User user) {
            usersBase.put(user.getId(), user);
            return usersBase.get(user.getId());
        }
    }
