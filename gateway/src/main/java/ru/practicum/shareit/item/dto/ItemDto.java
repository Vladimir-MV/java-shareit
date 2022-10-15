    package ru.practicum.shareit.item.dto;

    import lombok.*;
    import ru.practicum.shareit.user.dto.UserDto;
    import javax.validation.constraints.NotBlank;
    import java.util.List;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemDto {

        private Long id;
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotBlank
        private Boolean available;
        private UserDto owner;
        private List<CommentDto> comments;
        private Long requestId;
    }
