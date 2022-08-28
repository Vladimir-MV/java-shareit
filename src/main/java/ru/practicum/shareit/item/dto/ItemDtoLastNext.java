    package ru.practicum.shareit.item.dto;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.booking.model.LastBooking;
    import ru.practicum.shareit.booking.model.NextBooking;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.user.model.User;

    import java.util.List;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemDtoLastNext {

    //    public ItemDtoLastNext (Long id, String name, String description,
    //                            Boolean available, User owner, NextBooking nextBooking, List<CommentDto> comments){
    //        this.id = id;
    //        this.name = name;
    //        this.description = description;
    //        this.available = available;
    //        this.owner = owner;
    //        this.nextBooking = nextBooking;
    //        this.comments = comments;
    //    }
        public ItemDtoLastNext (Long id, String name, String description,
                                Boolean available, User owner){
            this.id = id;
            this.name = name;
            this.description = description;
            this.available = available;
            this.owner = owner;
        }
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private User owner;
        private LastBooking lastBooking;
        private NextBooking nextBooking;
        //private ItemRequest request;
        private List<CommentDto> comments;

    }


