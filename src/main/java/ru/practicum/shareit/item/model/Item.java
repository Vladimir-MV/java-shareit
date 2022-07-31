    package ru.practicum.shareit.item.model;

    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.requests.ItemRequest;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Item {
        public Item (String name, String description, Boolean available){
            this.name = name;
            this.description = description;
            this.available = available;
        }
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long owner;
        private ItemRequest request;
    }
