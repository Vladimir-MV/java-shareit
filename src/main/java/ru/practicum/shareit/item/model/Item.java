    package ru.practicum.shareit.item.model;

    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.requests.ItemRequest;
    import ru.practicum.shareit.user.model.User;
    import javax.validation.constraints.NotBlank;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Item {

        private Long id;
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotBlank
        private Boolean available;
        private User owner;
        private ItemRequest request;

        public Item (String name, String description, Boolean available){
            this.name = name;
            this.description = description;
            this.available = available;
        }
    }
