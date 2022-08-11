    package ru.practicum.shareit.item;

    import lombok.Getter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Repository;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.user.UserStorage;

    import java.util.*;

    @Repository
    public class ItemStorage {
        private Long id = 0L;
        @Getter
        private UserStorage userStorage;

        @Getter
        private Map<Long, Item> dataItem = new HashMap<>();

        @Autowired
        public ItemStorage (UserStorage userStorage){
            this.userStorage = userStorage;
        }

        public void getIdItem(Item item){
            ++id;
            item.setId(id);
        }

        public Item createItem(Item item, Long idUser) {
            dataItem.put(item.getId(), item);
            return dataItem.get(item.getId());
        }
        public List<Item> findAllItemsOwner (Long idUser){
            List<Item> list = new ArrayList<>();
            for (Item item: dataItem.values()){
                if (item.getOwner().getId() == idUser) list.add(item);
            }
            return list;
        }

        public Item findItemById (Long id) {
            if (!dataItem.containsKey(id))
                throw new NoSuchElementException("Такой вещи нет! findItemById()");
            return dataItem.get(id);
        }

        public Item patchItem (Item item) {
            dataItem.put(item.getId(), item);
            return dataItem.get(item.getId());
        }

        public Item deleteItem (Long id, Long idUser) {
            if (!dataItem.containsKey(id))
                throw new NoSuchElementException("Такой вещи нет! deleteItem()");
            if (dataItem.get(id).getOwner().getId() != idUser)
                throw new NoSuchElementException("Владелец вещи указан не верно! deleteItem()");
            Item item = dataItem.get(id);
            dataItem.remove(id);
            return item;
        }
    }
