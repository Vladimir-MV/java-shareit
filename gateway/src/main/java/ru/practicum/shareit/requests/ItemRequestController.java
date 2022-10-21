    package ru.practicum.shareit.requests;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import javax.validation.Valid;
    import javax.validation.constraints.Positive;
    import javax.validation.constraints.PositiveOrZero;

    @Controller
    @RequiredArgsConstructor
    @Slf4j
    @RequestMapping(path = "/requests")
    public class ItemRequestController {
        RequestsClient requestsClient;

        @Autowired
        public ItemRequestController(RequestsClient requestsClient) {
            this.requestsClient = requestsClient;
        }

        @PostMapping()
        public ResponseEntity<Object> createItemRequestGateway (@RequestHeader("X-Sharer-User-Id") long userId,
            @Valid  @RequestBody ItemRequestDto itemRequestDto) {
            log.info("createItemRequestGateway, userId={}, itemRequestDto {}", userId, itemRequestDto);
            return requestsClient.createItemRequest(userId, itemRequestDto);
        }
        @GetMapping()
        public ResponseEntity<Object> findAllItemRequestGateway(@RequestHeader("X-Sharer-User-Id") long userId) {
            log.info("findAllItemRequestGateway, userId={}", userId);
            return requestsClient.findAllItemRequest(userId);
        }

        @GetMapping("/all")
        public ResponseEntity<Object> findItemRequestGateway(@RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
            log.info("findItemRequestGateway, userId={}, from={}, size={}", userId, from, size);
            return requestsClient.findItemRequestPageable(userId, from, size);
        }
        @GetMapping("/{requestId}")
        public ResponseEntity<Object> findItemRequestByIdGateway(@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId) {
            log.info("findItemRequestByIdGateway, userId={}, requestId={}", userId, requestId);
            return requestsClient.findItemRequestById(userId, requestId);
        }


    }
