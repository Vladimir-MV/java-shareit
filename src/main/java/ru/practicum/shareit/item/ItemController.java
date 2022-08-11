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
            return itemService.findAllItemsOwner(idUser);
        }

        @GetMapping("/{id}")
        protected ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id) {
            return itemService.findItemById(id, idUser);
        }

        @PatchMapping("/{id}")
        protected ItemDto put(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id, @RequestBody ItemDto itemDto) throws ValidationException {
            return itemService.patchItem (itemDto, idUser, id);
        }

        @DeleteMapping("/{id}")
        protected ItemDto deleteItem(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @PathVariable Optional<Long> id) {
            return itemService.deleteItem(id, idUser);
        }

        @PostMapping()
        protected ItemDto create(@Valid @RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestBody ItemDto itemDto) throws ValidationException, ConflictException {
            return itemService.createItem(itemDto, idUser);
        }

        @GetMapping("/search")
        protected List<ItemDto> findItemById(@RequestHeader("X-Sharer-User-Id") Optional<Long> idUser,
            @RequestParam("text") String text) throws ValidationException {
            return itemService.findItemSearch(idUser, text);
        }
    }
