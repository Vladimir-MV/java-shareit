    package ru.practicum.shareit.booking;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.web.client.RestTemplateBuilder;
    import org.springframework.http.ResponseEntity;
    import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
    import org.springframework.stereotype.Service;
    import org.springframework.web.util.DefaultUriBuilderFactory;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.booking.dto.BookingDtoIn;
    import ru.practicum.shareit.client.BaseClient;
    import java.util.Map;

    @Service
    public class BookingClient extends BaseClient {
        private static final String API_PREFIX = "/bookings";

        @Autowired
        public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
            super(
                    builder
                            .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                            .build()
            );
        }

        public ResponseEntity<Object> findBookingsStateGateway (long userId, Integer from, Integer size, State state) {
            Map<String, Object> parameters = Map.of(
                    "state", state.name(),
                    "from", from,
                    "size", size
            );
            return get("?state={state}&from={from}&size={size}", userId, parameters);
        }
        public ResponseEntity<Object> findBookingsOwnerStateGateway (long userId, Integer from, Integer size, State state) {
            Map<String, Object> parameters = Map.of(
                    "state", state.name(),
                    "from", from,
                    "size", size
            );
            return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
        }


        public ResponseEntity<Object> createBookingGateway (long userId, BookingDtoIn bookingDto) {
            return post("", userId, bookingDto);
        }

        public ResponseEntity<Object> findBookingsAllByIdGateway (long userId) {
            return get("/", userId);
        }
        public ResponseEntity<Object> getBookingGateway (long userId, long bookingId) {
            return get("/" + bookingId, userId);
        }
        public ResponseEntity<Object> patchStatusBookingGateway (long userId, long bookingId, Boolean approved) {
            Map<String, Object> parameters = Map.of(
                    "approved", approved
            );
            return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
        }
    }
