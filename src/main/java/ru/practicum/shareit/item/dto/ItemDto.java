    package ru.practicum.shareit.item.dto;

    import lombok.*;
    import ru.practicum.shareit.user.model.User;
    import javax.validation.constraints.NotBlank;
    import java.util.List;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemDto {
        public ItemDto (Long id, String name, String description, Boolean available, User owner){
            this.id = id;
            this.name = name;
            this.description = description;
            this.available = available;
            this.owner = owner;
        }
        private Long id;
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotBlank
        private Boolean available;
        private User owner;
        private List<CommentDto> comments;
        //private ItemRequest request;
    }
