    package ru.practicum.shareit.item.model;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.user.model.User;
    import java.time.LocalDateTime;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Comment {
        public Comment (Long id, String text) {
            this.id = id;
            this.text = text;
        }

        private Long id;
        private String text;
        private Item item;
        private User author;
        private LocalDateTime created;


    }
