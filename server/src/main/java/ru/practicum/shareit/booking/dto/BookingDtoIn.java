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
        public class BookingDtoIn {

                private Long itemId;
                private LocalDateTime start;
                private LocalDateTime end;
        }
