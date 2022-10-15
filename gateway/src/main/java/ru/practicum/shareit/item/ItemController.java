    package ru.practicum.shareit.item;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import javax.validation.Valid;
    import javax.validation.constraints.Positive;
    import javax.validation.constraints.PositiveOrZero;

    @Controller
    @RequestMapping("/items")
    @RequiredArgsConstructor
    @Slf4j
    public class ItemController {
        ItemClient itemClient;

        @Autowired
        public ItemController(ItemClient itemClient) {
            this.itemClient = itemClient;
        }

        @GetMapping()
        public ResponseEntity<Object> findAllItemsGateway (@RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
            log.info("findAllItemsGateway, userId={}, from={}, size={}", userId, from, size);
            return itemClient.findAllItemsOwner(userId, from, size);
        }

        @GetMapping("/{itemId}")
        public ResponseEntity<Object> findItemByIdGateway (@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
            log.info("findItemByIdGateway, userId={}, id={}", userId, itemId);
            return itemClient.findItemById(userId, itemId);
        }
        @PatchMapping("/{itemId}")
        public ResponseEntity<Object> patchItemGateway (@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId, @RequestBody ItemDto itemDto) {
            log.info("patchItemGateway, userId={}, id={}, itemDto {}", userId, itemId, itemDto);
            return itemClient.patchItem (userId, itemId, itemDto);
        }

        @DeleteMapping("/{itemId}")
        public ResponseEntity<Object> deleteItemGateway (@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
            log.info("deleteItemGateway, userId={}, itemId={}", userId, itemId);
            return itemClient.deleteItem(userId, itemId);
        }

        @PostMapping()
        public ResponseEntity<Object> createItemGateway (@Valid @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemDto itemDto) {
            log.info("createItemGateway, userId={}, itemDto {}", userId, itemDto);
            return itemClient.createItem(userId, itemDto);
        }

        @GetMapping("/search")
        public ResponseEntity<Object> findItemByIdSearchGateway (@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam("text") String text,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
            log.info("findItemByIdSearchGateway, userId={}, text {}, from={}, size={}", userId, text, from, size);
            return itemClient.findItemSearch(userId, text, from, size);
        }

        @PostMapping("/{itemId}/comment")
        public ResponseEntity<Object> createCommentGateway (@Valid @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId, @RequestBody CommentDto commentDto) {
            log.info("createCommentGateway, userId={}, itemId={}, commentDto {}", userId, itemId, commentDto);
            return itemClient.createComment(userId, itemId, commentDto);
        }
    }
