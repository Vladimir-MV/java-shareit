    package ru.practicum.shareit.item;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemMapper;
    import ru.practicum.shareit.item.model.Item;


    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/items")
    public class ItemController {
        ItemServiceImpl itemServiceImpl;
        @Autowired
        public ItemController(ItemServiceImpl itemServiceImpl) {
            this.itemServiceImpl = itemServiceImpl;
        }


        @RequestMapping(value ="/",produces = "application/json")
        public String getURLValue(HttpServletRequest request){
            String test = request.getRequestURI();
            return test;
        }
        @GetMapping()
        protected List<ItemDto> findAllItemsOwner(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser) {
            List<ItemDto> list = new ArrayList<>();
            for (Item item: itemServiceImpl.findAllItemsOwnerService(idUser)){
                list.add(ItemMapper.toItemDto(item));
            }
            return list;
        }

        @GetMapping("/{id}")
        protected ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id) {
            return ItemMapper.toItemDto(itemServiceImpl.findItemByIdService(id, idUser));
        }

        @PatchMapping("/{id}")
        protected ItemDto put(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id, @RequestBody ItemDto itemDto) throws ValidationException, ConflictException {
            return ItemMapper.toItemDto(itemServiceImpl.patchItemService(ItemMapper.toItem(itemDto), idUser, id));
        }

        @DeleteMapping("/{id}")
        protected ItemDto deleteItem(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id) {
            return ItemMapper.toItemDto(itemServiceImpl.deleteItemService(id, idUser));
        }

        @PostMapping()
        protected ItemDto create(@Valid @RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestBody ItemDto itemDto) throws ValidationException, ConflictException {
            return ItemMapper.toItemDto(itemServiceImpl.createItemService(ItemMapper.toItem(itemDto), idUser));
        }
        @GetMapping("/search")
        protected List<ItemDto> findItemById(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestParam("text") String text) throws ValidationException {
            List<ItemDto> list = new ArrayList<>();
            for (Item item: itemServiceImpl.findItemSearchService(idUser, text)){
                list.add(ItemMapper.toItemDto(item));
            }
            return list;
        }
    }
