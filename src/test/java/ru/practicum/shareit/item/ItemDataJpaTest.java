    package ru.practicum.shareit.item;

    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import org.springframework.test.annotation.DirtiesContext;
    import ru.practicum.shareit.item.model.Item;
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
    public class ItemDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ItemRequestRepository itemRequestRepository;
        @Autowired
        private ItemRepository itemRepository;
        User user = new User(
                1L,
                "John",
                "john.doe@mail.com");
        User userNew = new User(
                2L,
                "Jack",
                "jack.doe@mail.com");
        ItemRequest itemRequest = new ItemRequest(
                1L,
                "description",
                user,
                LocalDateTime.of(2022, 4, 4, 4, 4, 4));
        Item item = new Item(
                1L,
                "отвертка",
                "хорошая",
                true,
                userNew,
                itemRequest);

        @Test
        void createItemDataJpaTest() {
            userRepository.save(user);
            userRepository.save(userNew);
            itemRequestRepository.save(itemRequest);
            itemRepository.save(item);
            TypedQuery<Item> query = em.getEntityManager().createQuery("Select i from Item i where i.id = :id", Item.class);
            Item itemQuery = query
                    .setParameter("id", item.getId())
                    .getSingleResult();

            assertThat(itemQuery.getId(), equalTo(item.getId()));
            assertThat(itemQuery.getName(), equalTo(item.getName()));
            assertThat(itemQuery.getDescription(), equalTo(item.getDescription()));
            assertThat(itemQuery.getOwner().getName(), equalTo(item.getOwner().getName()));
        }
    }
