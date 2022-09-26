    package ru.practicum.shareit.booking;

    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.TypedQuery;
    import java.time.LocalDateTime;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    @ExtendWith(MockitoExtension.class)
    @DataJpaTest
    public class BookingDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private BookingRepository bookingRepository;

        @Test
        void createBookingDataJpaTest() {
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
