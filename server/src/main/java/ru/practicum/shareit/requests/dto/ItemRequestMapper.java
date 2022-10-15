    package ru.practicum.shareit.requests.dto;

    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.dto.UserMapper;
    import java.util.ArrayList;
    import java.util.List;

    public class ItemRequestMapper {

        public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
            return new ItemRequestDto(
                    itemRequest.getId(),
                    itemRequest.getDescription(),
                    UserMapper.toUserDto(itemRequest.getRequestor()),
                    itemRequest.getCreated()
            );
        }

        public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
            return new ItemRequest(
                    itemRequestDto.getId(),
                    itemRequestDto.getDescription(),
                    itemRequestDto.getRequestor(),
                    itemRequestDto.getCreated());
        }
        public static List<ItemRequestDto> toListItemRequestDto(List<ItemRequest> listItemRequest) {
            List<ItemRequestDto> listDto = new ArrayList<>();
            for (ItemRequest itemRequest : listItemRequest) {
                listDto.add(toItemRequestDto(itemRequest));
            }
            return listDto;
        }

    }
