package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    ItemDto createItemService (ItemDto item, Optional<Long> idUser) throws ValidationException;

    List<ItemDto> findAllItemsOwnerService (Optional<Long> idUser);

    ItemDto findItemByIdService (Optional<Long> id, Optional<Long> idUser);

    ItemDto patchItemService (ItemDto item, Optional<Long> idUser, Optional<Long> id) throws ValidationException;

    ItemDto deleteItemService (Optional<Long> id, Optional<Long> idUser);

    List<ItemDto> findItemSearchService (Optional<Long> idUser, String text) throws ValidationException;

    boolean validationItem(Item item, Optional<Long> idUser) throws ValidationException;

}
