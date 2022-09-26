    package ru.practicum.shareit.user;

    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.dto.UserMapper;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.TypedQuery;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    @ExtendWith(MockitoExtension.class)
    @DataJpaTest

    public class UserDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private UserRepository userRepository;

        @Test
        void createUserDataJpaTest() {
            UserDto userDto = new UserDto(
                    1L,
                    "John",
                    "john.doe@mail.com");
            userRepository.save(UserMapper.toUser(userDto));
            TypedQuery<User> query = em.getEntityManager().createQuery("Select u from User u where u.email = :email", User.class);
            User user = query
                    .setParameter("email", userDto.getEmail())
                    .getSingleResult();

            assertThat(user.getId(), notNullValue());
            assertThat(user.getName(), equalTo(userDto.getName()));
            assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        }
    }