package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    //private void getIdItemService (Item item){}

    Item createItemService (Item item, Optional<Long> idUser) throws ValidationException;

    List<Item> findAllItemsOwnerService (Optional<Long> idUser);

    Item findItemByIdService (Optional<Long> id, Optional<Long> idUser);

    Item patchItemService (Item item, Optional<Long> idUser, Optional<Long> id) throws ValidationException;

    Item deleteItemService (Optional<Long> id, Optional<Long> idUser);

    List<Item> findItemSearchService (Optional<Long> idUser, String text) throws ValidationException;

    boolean validationItem(Item item, Optional<Long> idUser) throws ValidationException;

}
