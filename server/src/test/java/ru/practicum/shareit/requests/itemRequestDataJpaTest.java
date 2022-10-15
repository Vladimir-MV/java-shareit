    package ru.practicum.shareit.requests;

    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import org.springframework.test.annotation.DirtiesContext;
    import ru.practicum.shareit.requests.ItemRequestRepository;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.TypedQuery;
    import java.time.LocalDateTime;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    @DataJpaTest
    @DirtiesContext
    public class itemRequestDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ItemRequestRepository itemRequestRepository;

        @Test
        void createItemRequestDataJpaTest() {
             User user = new User(
                    1L,
                    "John",
                    "john.doe@mail.com");
             ItemRequest itemRequest = new ItemRequest(
                    1L,
                    "description",
                     user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
            userRepository.save(user);
            itemRequestRepository.save(itemRequest);
            TypedQuery<ItemRequest> query = em.getEntityManager().createQuery("Select i from ItemRequest i where i.id = :id", ItemRequest.class);
            ItemRequest itemRequestQuery = query
                    .setParameter("id", itemRequest.getId())
                    .getSingleResult();

            assertThat(itemRequestQuery.getId(), equalTo(itemRequest.getId()));
            assertThat(itemRequestQuery.getRequestor().getName(), equalTo(itemRequest.getRequestor().getName()));
            assertThat(itemRequestQuery.getDescription(), equalTo(itemRequest.getDescription()));
            assertThat(itemRequestQuery.getRequestor().getName(), equalTo(itemRequest.getRequestor().getName()));
        }
    }



