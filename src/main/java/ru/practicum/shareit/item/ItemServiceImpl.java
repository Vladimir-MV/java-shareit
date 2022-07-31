    package ru.practicum.shareit.item;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.user.UserStorage;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;

    @Slf4j
    @Service
    public class ItemServiceImpl implements ItemService {

        private ItemStorage itemStorage;
        private UserStorage userStorage;

        @Autowired
        public ItemServiceImpl (ItemStorage itemStorage, UserStorage userStorage){
            this.itemStorage = itemStorage;
            this.userStorage = userStorage;
        }
        private void getIdItemService (Item item){
            itemStorage.getIdItemStorage(item);
        }

        public Item createItemService (Item item, Optional<Long> idUser) throws ValidationException {
            if (validationItem(item, idUser)) {
                Item itemInStorage = itemStorage.createItemStorage (item, idUser.get());
                log.info("Добавлена вещь: {}", itemInStorage.getName());
                return itemInStorage;
            }
            throw new ValidationException("Вещь не создана! createItemService()");
        }

        public List<Item> findAllItemsOwnerService (Optional<Long> idUser){
            if (!idUser.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! findAllItemsOwnerService()");
            List<Item> list = itemStorage.findAllItemsOwnerStorage (idUser.get());
            log.info("Текущее количество вещей пользователя {}, в списке: {}", idUser.get(), list.size());
            return list;
        }

        public Item findItemByIdService (Optional<Long> id, Optional<Long> idUser) {
            if (!idUser.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! findItemByIdService()");
            if (!id.isPresent())
                throw new NoSuchElementException("Не правильно задан id вещи! findItemByIdService()");
                Item item = itemStorage.findItemByIdStorage (id.get());
                log.info("Просмотрена вещь: {}", item.getName());
                return item;
        }

        public Item patchItemService (Item item, Optional<Long> idUser, Optional<Long> id) throws ValidationException {
            if (!id.isPresent())
                throw new NoSuchElementException("Отсутствует id вещи! patchItemService()");
            if (!idUser.isPresent())
                throw new ValidationException("Отсутствует id пользователя! validationItem()");
            Item itemSt = itemStorage.getDataItem().get(id.get());
            if (itemSt.getOwner() != (idUser.get()))
                throw new NoSuchElementException("Вещь не принадлежит этому владельцу! patchUserService()");
            if (!(item.getName() == null || item.getName() == "")) itemSt.setName(item.getName());
            if (!(item.getDescription() == null || item.getDescription() == ""))
                itemSt.setDescription(item.getDescription());
            if (item.getAvailable() != null) itemSt.setAvailable(item.getAvailable());
            itemStorage.patchItemStorage(itemSt);
                log.info("Данные вещи: {} изменены.", itemSt.getName());
                return itemSt;
        }

        public Item deleteItemService (Optional<Long> id, Optional<Long> idUser) {
            if (!idUser.isPresent())
                throw new NoSuchElementException("Отсутствует id пользователя! deleteItemService()");
            if (id.isPresent()) {
                Item item = itemStorage.deleteItemStorage (id.get(), idUser.get());
                log.info("Пользователь: {} удален.", item);
                return item;
            }
            throw new NoSuchElementException("Переменные пути указаны не верно! deleteItemService()");
        }

        public List<Item> findItemSearchService (Optional<Long> idUser, String text) throws ValidationException {
            if (!idUser.isPresent())
                throw new ValidationException("Отсутствует id пользователя! findItemSearchService()");
            if (text == null || text.length() == 0) return new ArrayList<Item>();
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
            return listItem;
        }

        public boolean validationItem(Item item, Optional<Long> idUser) throws ValidationException {
            if (!idUser.isPresent()) throw new ValidationException("Отсутствует id пользователя! validationItem()");
            if (!userStorage.getUsersBase().containsKey(idUser.get()))
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
