    package ru.practicum.shareit.item;

    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.TypedQuery;
    import java.time.LocalDateTime;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    @ExtendWith(MockitoExtension.class)
    @DataJpaTest
    public class ItemDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private ItemRepository itemRepository;

        @Test
        void createItemDataJpaTest() {
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
