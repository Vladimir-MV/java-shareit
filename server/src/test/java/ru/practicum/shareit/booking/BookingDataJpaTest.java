    package ru.practicum.shareit.booking;

    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import org.springframework.test.annotation.DirtiesContext;
    import ru.practicum.shareit.booking.BookingRepository;
    import ru.practicum.shareit.booking.Status;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.item.ItemRepository;
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
    public class BookingDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ItemRequestRepository itemRequestRepository;
        @Autowired
        private ItemRepository itemRepository;
        @Autowired
        private BookingRepository bookingRepository;
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
                false,
                userNew,
                itemRequest);
        Booking booking = new Booking(
                1L,
                LocalDateTime.of(2022, 10, 10, 10, 10, 10),
                LocalDateTime.of(2022, 11, 11, 11, 11, 11),
                item,
                userNew,
                Status.REJECTED);

        @Test
        void createBookingDataJpaTest() {
            userRepository.save(user);
            userRepository.save(userNew);
            itemRequestRepository.save(itemRequest);
            itemRepository.save(item);
            bookingRepository.save(booking);
            TypedQuery<Booking> query = em.getEntityManager().createQuery("Select i from Booking i where i.id = :id", Booking.class);
            Booking bookingQuery = query
                    .setParameter("id", booking.getId())
                    .getSingleResult();

            assertThat(bookingQuery.getId(), equalTo(booking.getId()));
            assertThat(bookingQuery.getBooker().getName(), equalTo(booking.getBooker().getName()));
            assertThat(bookingQuery.getStatus(), equalTo(booking.getStatus()));
            assertThat(bookingQuery.getItem().getName(), equalTo(booking.getItem().getName()));
        }
    }
