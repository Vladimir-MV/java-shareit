package ru.practicum.shareit.booking.dto;

import lombok.Setter;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
    import java.time.LocalDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor

    public class BookingDtoLastNextOut {
        private Long Id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
    }
