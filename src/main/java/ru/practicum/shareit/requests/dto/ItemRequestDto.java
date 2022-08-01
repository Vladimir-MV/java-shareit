    package ru.practicum.shareit.requests.dto;

    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.user.model.User;

    import java.time.LocalDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    public class ItemRequestDto {
        private Long id;
        private String description;
        private User requestor;
        private LocalDateTime created;
    }
