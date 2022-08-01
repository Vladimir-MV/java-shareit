    package ru.practicum.shareit.item;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.dto.ItemDto;
    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/items")
    public class ItemController {
        ItemService itemService;
        @Autowired
        public ItemController(ItemService itemService) {
            this.itemService = itemService;
        }


        @RequestMapping(value ="/", produces = "application/json")
        public String getURLValue(HttpServletRequest request){
            String test = request.getRequestURI();
            return test;
        }
        @GetMapping()
        protected List<ItemDto> findAllItemsOwner(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser) {
            return itemService.findAllItemsOwnerService(idUser);
        }

        @GetMapping("/{id}")
        protected ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id) {
            return itemService.findItemByIdService(id, idUser);
        }

        @PatchMapping("/{id}")
        protected ItemDto put(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id, @RequestBody ItemDto itemDto) throws ValidationException {
            return itemService.patchItemService (itemDto, idUser, id);
        }

        @DeleteMapping("/{id}")
        protected ItemDto deleteItem(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id) {
            return itemService.deleteItemService(id, idUser);
        }

        @PostMapping()
        protected ItemDto create(@Valid @RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestBody ItemDto itemDto) throws ValidationException, ConflictException {
            return itemService.createItemService(itemDto, idUser);
        }

        @GetMapping("/search")
        protected List<ItemDto> findItemById(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestParam("text") String text) throws ValidationException {
            return itemService.findItemSearchService(idUser, text);
        }
    }
