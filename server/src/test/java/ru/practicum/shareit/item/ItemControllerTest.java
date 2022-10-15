    package ru.practicum.shareit.item;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
    import org.springframework.boot.test.mock.mockito.MockBean;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;
    import ru.practicum.shareit.booking.dto.BookingDtoIn;
    import ru.practicum.shareit.booking.dto.BookingDtoLastNextOut;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
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
    @WebMvcTest(controllers = ItemController.class)
    class ItemControllerTest {
//        @InjectMocks
//        private  itemController;
        @Autowired
        private ObjectMapper mapper;
        @Autowired
        private MockMvc mvc;
        @MockBean
        ItemService itemService;
        private ItemDto itemDto;
        private CommentDto commentDto;
        private ItemDtoLastNext itemDtoLastNext;
        private UserDto userDto;
        private List<ItemDto> list;
        private List<ItemDtoLastNext> lastNextList;
        String text;


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

           itemDtoLastNext = new ItemDtoLastNext(
                   2L,
                   "john",
                   "description",
                   true,
                   new BookingDtoLastNextOut(1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusMonths(2)),
                   new BookingDtoLastNextOut(3L, 4L, LocalDateTime.now(), LocalDateTime.now().plusMonths(3)),
                   Arrays.asList(new CommentDto()),
                   new ItemRequestDto(1L, "description", userDto, LocalDateTime.now(), list));
           list = Arrays.asList(itemDto);
           lastNextList = Arrays.asList(itemDtoLastNext);
           text = "text";
       }

        @Test
        void createItemTest() throws Exception{

            when(itemService.createItem(any(), any()))
                    .thenReturn(itemDto);

            mvc.perform(post("/items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(itemDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(itemDto)));
        }

        @Test
        void findItemByIdTest() throws Exception{
            when(itemService.findItemById(any(), any()))
                    .thenReturn(itemDtoLastNext);

            mvc.perform(get("/items/2")
                            //.content(mapper.writeValueAsString(itemDtoLastNext))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(itemDtoLastNext)));
        }

        @Test
        void deleteItemTest() throws Exception{
            when(itemService.deleteItem(any(), any()))
                    .thenReturn(itemDto);

            mvc.perform(delete("/items/1")
                            //.content(mapper.writeValueAsString(itemDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(itemDto)));
        }

        @Test
        void findAllItemsTest() throws Exception{
            when(itemService.findAllItemsOwner(any(), any(), any()))
                    .thenReturn(lastNextList);

            mvc.perform(get("/items")
                            //.content(mapper.writeValueAsString(lastNextList))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(lastNextList)));

        }

        @Test
        void findItemByIdSearchTest() throws Exception{
            when(itemService.findItemSearch(any(), any(), any(), any()))
                    .thenReturn(list);

            mvc.perform(get("/items/search")
                            .param("text", text)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(list)));
        }

        @Test
        void putTest() throws Exception{
            when(itemService.patchItem(any(), any(), any()))
                    .thenReturn(itemDto);

            mvc.perform(patch("/items/1")
                            .content(mapper.writeValueAsString(itemDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(itemDto)));
        }
        @Test
        void createTest() throws Exception {
            when(itemService.createComment(any(), any(), any()))
                    .thenReturn(commentDto);

            mvc.perform(post("/items/{id}/comment", 3)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(commentDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(commentDto)));
        }
    }