    package ru.practicum.shareit.requests.dto;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.model.User;
    import javax.validation.constraints.NotBlank;
    import java.time.LocalDateTime;
    import java.util.List;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemRequestDto {
        public ItemRequestDto(Long id, String description, UserDto requestor, LocalDateTime created) {
            this.id = id;
            this.description = description;
            this.requestor = requestor;
            this.created = created;
        }

        private Long id;
        @NotBlank
        private String description;
        private UserDto requestor;
        private LocalDateTime created;
        private List<ItemDto> items;

    }
