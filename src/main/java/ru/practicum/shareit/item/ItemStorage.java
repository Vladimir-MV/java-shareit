    package ru.practicum.shareit.item;

    import lombok.Getter;
    import org.springframework.stereotype.Repository;
    import ru.practicum.shareit.item.model.Item;

    import java.util.*;

    @Repository
    public class ItemStorage {
        private Long id = 0L;
        @Getter
        private Map<Long, Item> dataItem = new HashMap<>();

        public void getIdItemStorage(Item item){
            ++id;
            item.setId(id);
        }

        public Item createItemStorage (Item item, Long idUser) {
            item.setOwner(idUser);
            dataItem.put(item.getId(), item);
            return dataItem.get(item.getId());
        }
        public List<Item> findAllItemsOwnerStorage (Long idUser){
            List<Item> list = new ArrayList<>();
            for (Item item: dataItem.values()){
                if (item.getOwner() == idUser) list.add(item);
            }
            return list;
        }

        public Item findItemByIdStorage (Long id) {
            if (!dataItem.containsKey(id))
                throw new NoSuchElementException("Такой вещи нет! findItemByIdStorage()");
            return dataItem.get(id);
        }

        public Item patchItemStorage (Item item) {
            dataItem.put(item.getId(), item);
            return dataItem.get(item.getId());
        }

        public Item deleteItemStorage (Long id, Long idUser) {
            if (!dataItem.containsKey(id))
                throw new NoSuchElementException("Такой вещи нет! deleteItemStorage()");
            if (dataItem.get(id).getOwner() != idUser)
                throw new NoSuchElementException("Владелец вещи указан не верно! deleteItemStorage()");
            Item item = dataItem.get(id);
            dataItem.remove(id);
            return item;
        }
    }
