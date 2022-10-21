    package ru.practicum.shareit.booking;

    import org.junit.jupiter.api.Test;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
    import org.springframework.boot.test.mock.mockito.MockBean;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.nio.charset.StandardCharsets;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.when;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
    import static ru.practicum.shareit.booking.Status.WAITING;
    @ExtendWith(MockitoExtension.class)
    @WebMvcTest(controllers = BookingController.class)
    class BookingControllerTest {
        @Autowired
        private ObjectMapper mapper;
        @Autowired
        private MockMvc mvc;
        @MockBean
        BookingService bookingService;
        private ItemDto itemDto;
        private CommentDto commentDto;
        private ItemDtoLastNext itemDtoLastNext;
        private UserDto userDto;
        private BookingDto bookingDtoLastNextOut;
        BookingDto bookingDto2;
        BookingDto bookingDto;
        Boolean approved;
        private List<ItemDto> list;
        private List<BookingDto> bookingList;
        String state;
        Integer from;
        Integer size;


        @BeforeEach
        void setUp() {
            mapper.findAndRegisterModules();
            userDto = new UserDto(
                    1L,
                    "John",
                    "john.doe@mail.com");
            itemDto = new ItemDto(
                    1L,
                    "john",
                    "John good",
                    true,
                    userDto,
                    4L);
            commentDto = new CommentDto(
                    3L,
                    "норм",
                    itemDto,
                    "john",
                    LocalDateTime.now());
            bookingDtoLastNextOut = new BookingDto(
                    3L,

                    1L,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMonths(3),
                    WAITING,
                    userDto,
                    itemDto);

            itemDtoLastNext = new ItemDtoLastNext(
                    2L,
                    "john",
                    "description",
                    true,
                    bookingDtoLastNextOut,
                    new BookingDto(
                            3L,
                            1L,
                            LocalDateTime.now(),
                            LocalDateTime.now().plusMonths(3),
                            WAITING,
                            userDto,
                            itemDto),
                    Arrays.asList(new CommentDto()),
                    new ItemRequestDto(
                            1L,
                            "description",
                            userDto,
                            LocalDateTime.now(),
                            list));
            bookingDto2 = new BookingDto(
                    1L,
                    1L,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMonths(2),
                    WAITING,
                    userDto,
                    itemDto);
            bookingDto = new BookingDto(
                    3L,
                    1L,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMonths(2),
                    WAITING,
                    userDto,
                    itemDto);
            approved = true;
            list = Arrays.asList(itemDto);
            bookingList = new ArrayList<>();
            state = "state";
            from = 1;
            size = 4;
        }
        @Test
        void createTest() throws Exception {
            when(bookingService.createBooking(any(), any()))
                    .thenReturn(bookingDto);

            mvc.perform(post("/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookingDto2))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
        }
        @Test
        void putStatusTest() throws Exception {
            when(bookingService.patchStatusBooking(any(), any(), any()))
                    .thenReturn(bookingDto);

            mvc.perform(patch("/bookings/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(bookingDto))
                            .param("approved", String.valueOf(approved))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
        }

        @Test
        void findBookingIdTest() throws Exception {
            when(bookingService.findBookingById(any(), any()))
                    .thenReturn(bookingDto);

            mvc.perform(get("/bookings/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
        }

        @Test
        void findBookingTest() throws Exception {
            when(bookingService.findBookingsState(any(), any(), any(), any()))
                    .thenReturn(bookingList);

            mvc.perform(get("/bookings")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size))
                            .param("state", state)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(bookingList)));
        }

        @Test
        void findBookingSlashTest() {
        }

        @Test
        void findBookingOwnerTest() throws Exception {
            when(bookingService.findBookingsOwnerState(any(), any(), any(), any()))
                    .thenReturn(bookingList);

            mvc.perform(get("/bookings/owner")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size))
                            .param("state", state)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(bookingList)));
        }
    }