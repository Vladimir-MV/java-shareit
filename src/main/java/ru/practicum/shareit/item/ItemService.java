package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    ItemDto createItem (ItemDto item, Optional<Long> idUser) throws ValidationException;

    List<ItemDto> findAllItemsOwner (Optional<Long> idUser);

    ItemDto findItemById (Optional<Long> id, Optional<Long> idUser);

    ItemDto patchItem (ItemDto item, Optional<Long> idUser, Optional<Long> id) throws ValidationException;

    ItemDto deleteItem(Optional<Long> id, Optional<Long> idUser);

    List<ItemDto> findItemSearch (Optional<Long> idUser, String text) throws ValidationException;

    boolean validationItem(Item item, Optional<Long> idUser) throws ValidationException;

}
