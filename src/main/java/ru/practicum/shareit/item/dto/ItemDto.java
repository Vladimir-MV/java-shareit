    package ru.practicum.shareit.item.dto;

    import lombok.*;
    import ru.practicum.shareit.requests.ItemRequest;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemDto {
        private String name;
        private String description;
        private Boolean available;
        private Long id;
        private Long owner;
        private ItemRequest request;
        private int length;
        public ItemDto (String name, String description, Boolean available, Long id){
            this.name = name;
            this.description = description;
            this.available = available;
            this.id = id;
        }
    }
