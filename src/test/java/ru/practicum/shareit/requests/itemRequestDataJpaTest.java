    package ru.practicum.shareit.requests;

    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.TypedQuery;
    import java.time.LocalDateTime;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    @ExtendWith(MockitoExtension.class)
    @DataJpaTest

    public class itemRequestDataJpaTest {
        @Autowired
        private TestEntityManager em;
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



