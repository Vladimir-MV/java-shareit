    package ru.practicum.shareit.booking.dto;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.booking.model.Status;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import javax.validation.constraints.NotNull;
    import java.time.LocalDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class BookingDto {

        private Long id;
        @NotNull
        private Long itemId;
        @NotNull
        private LocalDateTime start;
        @NotNull
        private LocalDateTime end;
        private Long bookerId;
        private Status status;
        private UserDto booker;
        private ItemDto item;


    }
