        package ru.practicum.shareit.booking.dto;

        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import ru.practicum.shareit.booking.Status;
        import java.time.LocalDateTime;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public class BookingDtoById {

        public BookingDtoById(Long id) {
                Id = id;
        }

        private Long Id;
        private LocalDateTime start;
        private LocalDateTime end;
        private Status status;
        private Long bookerId;
        private Long itemId;
        private String itemName;


         }
