    package ru.practicum.shareit.item.dto;

    import ru.practicum.shareit.item.model.Item;

    public class ItemMapper {

        public static ItemDto toItemDto(Item item) {
            return new ItemDto(
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequest() != null ? item.getRequest().getId() : item.getId()
            );
        }
        public static Item toItem(ItemDto itemDto) {
            return new Item (
                    itemDto.getName(),
                    itemDto.getDescription(),
                    itemDto.getAvailable()
            );
        }
    }