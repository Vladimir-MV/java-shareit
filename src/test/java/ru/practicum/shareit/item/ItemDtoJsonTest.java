    package ru.practicum.shareit.item;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import ru.practicum.shareit.booking.dto.BookingDtoById;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.util.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.json.JsonTest;
    import org.springframework.boot.test.json.JacksonTester;
    import org.springframework.boot.test.json.JsonContent;
    import static org.assertj.core.api.Assertions.assertThat;

    @JsonTest
    public class ItemDtoJsonTest {
        @Autowired
        private JacksonTester<ItemDto> jsonDto;
        @Autowired
        private JacksonTester<ItemDtoLastNext> jsonDtoLastNext;
        ItemDto itemDto;
        ItemDtoLastNext itemDtoLastNext;

        @BeforeEach
        void setUp() {
            itemDto = new ItemDto(
                    1L,
                    "John",
                    "John good",
                    true,
                    new UserDto(),
                    4L);
            CommentDto commentDto = new CommentDto();
            itemDtoLastNext = new ItemDtoLastNext (
                    2L,
                    "John",
                    "description",
                    true,
                    new BookingDtoById(),
                    new BookingDtoById(),
                    List.of(commentDto),
                    new ItemRequestDto());
        }

        @Test
        void testItemDto() throws Exception {
            JsonContent<ItemDto> result = jsonDto.write(itemDto );
            //JsonNode jsonNode= mapper.readValue(mapper.writeValueAsString(userDto), JsonNode.class);
            assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
            assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
            assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("John good");
            assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
            assertThat(result).doesNotHaveEmptyJsonPathValue("$.owner");
            assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(4);
        }

        @Test
        void testItemDtoLastNext() throws Exception {
            JsonContent<ItemDtoLastNext> result2 = jsonDtoLastNext.write(itemDtoLastNext);
            assertThat(result2).extractingJsonPathNumberValue("$.id").isEqualTo(2);
            assertThat(result2).extractingJsonPathStringValue("$.name").isEqualTo("John");
            assertThat(result2).extractingJsonPathStringValue("$.description").isEqualTo("description");
            assertThat(result2).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
            assertThat(result2).doesNotHaveEmptyJsonPathValue("$.lastBooking");
            assertThat(result2).doesNotHaveEmptyJsonPathValue("$.nextBooking");
            assertThat(result2).doesNotHaveEmptyJsonPathValue("$.comments");
            assertThat(result2).doesNotHaveEmptyJsonPathValue("$.request");
        }
    }
