    package ru.practicum.shareit.booking.dto;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.booking.Status;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.time.LocalDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class BookingDto {



        public BookingDto(Long id, Long bookerId, LocalDateTime start, LocalDateTime end, Status status, UserDto booker, ItemDto item)
        {
            this.id = id;
            this.bookerId = bookerId;
            this.start = start;
            this.end = end;
            this.status = status;
            this.booker = booker;
            this.item = item;
        }

        private Long id;
        private Long itemId;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
        private Status status;
        private UserDto booker;
        private ItemDto item;


    }
