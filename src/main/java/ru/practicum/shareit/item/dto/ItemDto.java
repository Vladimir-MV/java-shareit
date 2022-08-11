    package ru.practicum.shareit.item.dto;

    import lombok.*;
    import ru.practicum.shareit.requests.ItemRequest;
    import ru.practicum.shareit.user.model.User;
    import javax.validation.constraints.NotBlank;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemDto {
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotBlank
        private Boolean available;
        private Long id;
        private User owner;
        private ItemRequest request;
        private int length;
        public ItemDto (String name, String description, Boolean available, Long id){
            this.name = name;
            this.description = description;
            this.available = available;
            this.id = id;
        }
    }
