    package ru.practicum.shareit.item;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemMapper;
    import ru.practicum.shareit.item.model.Item;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;

    @Slf4j
    @Service
    public class ItemServiceImpl implements ItemService {

        private ItemStorage itemStorage;

        @Autowired
        public ItemServiceImpl (ItemStorage itemStorage){
            this.itemStorage = itemStorage;
        }
        private void getIdItemService (Item item){
            itemStorage.getIdItem(item);
        }

        public ItemDto createItem (ItemDto itemDto, Optional<Long> idUser) throws ValidationException {
            Item item = ItemMapper.toItem(itemDto);
            if (validationItem(item, idUser)) {
                item.setOwner(itemStorage.getUserStorage().getUsersBase().get(idUser.get()));
                Item itemInStorage = itemStorage.createItem(item, idUser.get());
                log.info("Добавлена вещь: {}", itemInStorage.getName());
                return ItemMapper.toItemDto(itemInStorage);
            }
            throw new ValidationException("Вещь не создана! createItem()");
        }

        public List<ItemDto> findAllItemsOwner (Optional<Long> idUser){
            if (!idUser.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! findAllItemsOwner()");
            List<Item> list = itemStorage.findAllItemsOwner (idUser.get());
            log.info("Текущее количество вещей пользователя {}, в списке: {}", idUser.get(), list.size());
            List<ItemDto> listDto = new ArrayList<>();
            for (Item item: list){
                listDto.add(ItemMapper.toItemDto(item));
            }
            return listDto;
        }

        public ItemDto findItemById (Optional<Long> id, Optional<Long> idUser) {
            if (!idUser.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! findItemById()");
            if (!id.isPresent())
                throw new NoSuchElementException("Не правильно задан id вещи! findItemById()");
                Item item = itemStorage.findItemById (id.get());
                log.info("Просмотрена вещь: {}", item.getName());
                return ItemMapper.toItemDto(item);
        }

        public ItemDto patchItem (ItemDto itemDto, Optional<Long> idUser, Optional<Long> id) throws ValidationException {
            Item item = ItemMapper.toItem(itemDto);
            if (!id.isPresent())
                throw new NoSuchElementException("Отсутствует id вещи! patchItem()");
            if (!idUser.isPresent())
                throw new ValidationException("Отсутствует id пользователя! validationItem()");
            Item itemSt = itemStorage.getDataItem().get(id.get());
            if (itemSt.getOwner().getId() != (idUser.get()))
                throw new NoSuchElementException("Вещь не принадлежит этому владельцу! patchUser()");
            if (!(item.getName() == null || item.getName() == "")) itemSt.setName(item.getName());
            if (!(item.getDescription() == null || item.getDescription() == ""))
                itemSt.setDescription(item.getDescription());
            if (item.getAvailable() != null) itemSt.setAvailable(item.getAvailable());
            itemStorage.patchItem(itemSt);
                log.info("Данные вещи: {} изменены.", itemSt.getName());
                return ItemMapper.toItemDto(itemSt);
        }

        public ItemDto deleteItem(Optional<Long> id, Optional<Long> idUser) {
            if (!idUser.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! deleteItemService()");
            if (id.isPresent()) {
                Item item = itemStorage.deleteItem (id.get(), idUser.get());
                log.info("Пользователь: {} удален.", item);
                return ItemMapper.toItemDto(item);
            }
            throw new NoSuchElementException("Переменные пути указаны не верно! deleteItemService()");
        }

        public List<ItemDto> findItemSearch (Optional<Long> idUser, String text) throws ValidationException {
            if (!idUser.isPresent())
                throw new ValidationException("Отсутствует id пользователя! findItemSearch()");
            if (text == null || text.length() == 0) return new ArrayList<ItemDto>();
            List<Item> listItem = new ArrayList<>();
            String textLowerCase = text.toLowerCase();
            for (Item item: itemStorage.getDataItem().values()){
                if (!item.getAvailable()) continue;;
                boolean flag = false;
                String[] textSetDescription = item.getDescription().toLowerCase().split(" ");
                String[] textSetName = item.getName().toLowerCase().split(" ");
                for (String textDescription: textSetDescription) {
                    if (textDescription.indexOf(textLowerCase) >= 0) flag = true;
                }
                for (String textName: textSetName) {
                    if (textName.indexOf(textLowerCase) >= 0) flag = true;
                }
                if (flag){
                    listItem.add(item);
                }
            }
            List<ItemDto> listDto = new ArrayList<>();
            for (Item item: listItem){
                listDto.add(ItemMapper.toItemDto(item));
            }
            return listDto;
        }

        public boolean validationItem(Item item, Optional<Long> idUser) throws ValidationException {
            if (!idUser.isPresent()) throw new ValidationException("Отсутствует id пользователя! validationItem()");
            if (!itemStorage.getUserStorage().getUsersBase().containsKey(idUser.get()))
                throw new NoSuchElementException ("Указанный идентификатор пользователя не найден! validationItem()");
            if (item.getName() == null || item.getName() == "")
                throw new ValidationException ("Не указаны данные - name для создания вещи! validationItem()");
            if (item.getDescription() == null || item.getDescription() == "")
                throw new ValidationException ("Не указаны данные - description для создания вещи! validationItem()");
            if (item.getAvailable() == null)
                throw new ValidationException ("Не указаны данные - available для создания вещи! validationItem()");
            if (item.getId() == null) {
                getIdItemService(item);
            }
            else if (item.getId() <= 0)  {
                throw new NoSuchElementException("id не может быть отрицательным! validationItem()");
            }
            return true;
        }
    }
