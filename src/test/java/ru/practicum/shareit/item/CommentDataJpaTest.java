    package ru.practicum.shareit.item;

    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import ru.practicum.shareit.item.model.Comment;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.TypedQuery;
    import java.time.LocalDateTime;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    @ExtendWith(MockitoExtension.class)
    @DataJpaTest
    public class CommentDataJpaTest {
        @Autowired
        private TestEntityManager em;
        @Autowired
        private CommentRepository commentRepository;
        private User user = new User(
                    1L,
                    "John",
                    "john.doe@mail.com");
        private ItemRequest itemRequest = new ItemRequest(
                    1L,
                    "description",
                    user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));
        private Item item = new Item(
                    1L,
                    "отвертка",
                    "хорошая",
                    false,
                    user,
                    itemRequest);
        private Comment comment = new Comment(
                    1L,
                    "норм",
                    item,
                    user,
                    LocalDateTime.of(2022, 4, 4, 4, 4, 4));

        @Test
        void createCommentRepositoryDataJpaTest() {

            commentRepository.save(comment);
            TypedQuery<Comment> query = em.getEntityManager().createQuery("Select c from Comment c where c.id = :id", Comment.class);
            Comment commentQuery = query
                    .setParameter("id", comment.getId())
                    .getSingleResult();

            assertThat(commentQuery.getId(), equalTo(comment.getId()));
            assertThat(commentQuery.getText(), equalTo(comment.getText()));
            assertThat(commentQuery.getItem().getName(), equalTo(comment.getItem().getName()));
            assertThat(commentQuery.getAuthor().getName(), equalTo(comment.getAuthor().getName()));
        }
    }
