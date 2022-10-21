    package ru.practicum.shareit.requests;

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
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.nio.charset.StandardCharsets;
    import java.time.LocalDateTime;
    import java.util.Arrays;
    import java.util.List;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.when;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @ExtendWith(MockitoExtension.class)
    @WebMvcTest(controllers = ItemRequestController.class)
    class ItemRequestControllerTest {
        @Autowired
        private ObjectMapper mapper;
        @Autowired
        private MockMvc mvc;
        @MockBean
        ItemRequestService itemRequestService;
        private ItemRequestDto itemRequestDto;
        private ItemDto itemDto;
        private UserDto userDto;
        private List<ItemDto> list;
        private List<ItemRequestDto> requestDtoList;

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

            list = Arrays.asList(itemDto);
            itemRequestDto = new ItemRequestDto(1L, "description", userDto, LocalDateTime.now(), list);
            requestDtoList = Arrays.asList(itemRequestDto);
        }

        @Test
        void createTest() throws Exception {
            when(itemRequestService.createItemRequest(any(), any()))
                    .thenReturn(itemRequestDto);

            mvc.perform(post("/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(itemRequestDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
        }

        @Test
        void findAllTest() throws Exception {
            when(itemRequestService.findAllItemRequest(any()))
                    .thenReturn(requestDtoList);

            mvc.perform(get("/requests")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(requestDtoList)));
        }

        @Test
        void findItemRequestTest() throws Exception {
            when(itemRequestService.findItemRequestPageable(any(), any(), any()))
                    .thenReturn(requestDtoList);

            mvc.perform(get("/requests/all")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(requestDtoList)));
        }

        @Test
        void testFindItemRequestTest() throws Exception {
            when(itemRequestService.findItemRequestById(any(), any()))
                    .thenReturn(itemRequestDto);

            mvc.perform(get("/requests/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
        }
    }