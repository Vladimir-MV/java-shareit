    package ru.practicum.shareit.requests.model;

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
    public class ItemRequest {

        private Long id;
        private String description;
        private User requestor;
        private LocalDateTime created;
    }
