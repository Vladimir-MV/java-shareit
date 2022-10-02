        package ru.practicum.shareit.booking.dto;

        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import java.time.LocalDateTime;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public class BookingDtoById {

//        public BookingDtoById(Long id, Long bookerId, LocalDateTime start, LocalDateTime end) {
//                this.id = id;
//                this.bookerId = bookerId;
//                this.start = start;
//                this.end = end;
//        }
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
        }
