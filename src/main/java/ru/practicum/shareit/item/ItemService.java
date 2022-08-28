    package ru.practicum.shareit.item;

    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import java.util.List;
    import java.util.Optional;

    public interface ItemService {

        List<ItemDtoLastNext> findAllItemsOwner (Optional<Long> idUser) throws ValidationException;
        List<ItemDto> findItemSearch (Optional<Long> idUser, String text) throws ValidationException;
        CommentDto createComment(Optional<Long> idUser, Optional<Long> itemId, CommentDto text) throws ValidationException;
        ItemDtoLastNext findItemById(Optional<Long> idUser, Optional<Long> id) throws ValidationException;
        ItemDto patchItem(Optional<Long> idUser, ItemDto itemDto, Optional<Long> id) throws ValidationException;
        ItemDto createItem(Optional<Long> idUser, ItemDto itemDto) throws ValidationException;
        ItemDto deleteItem(Optional<Long> idUser, Optional<Long> id) throws ValidationException;
    }

