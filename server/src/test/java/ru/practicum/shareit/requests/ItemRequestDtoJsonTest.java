    package ru.practicum.shareit.requests;

    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.autoconfigure.json.JsonTest;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.user.dto.UserDto;
    import java.time.LocalDateTime;
    import java.util.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.json.JacksonTester;
    import org.springframework.boot.test.json.JsonContent;
    import static org.assertj.core.api.Assertions.assertThat;

    @JsonTest
    public class ItemRequestDtoJsonTest {
        @Autowired
        private JacksonTester<ItemRequestDto> jsonDto;
        private LocalDateTime time = LocalDateTime.of(2022, 2, 3, 4, 5, 6);
        ItemDto itemDto = new ItemDto(
                5L,
                "John",
                "John good",
                true,
                new UserDto(),
                4L);
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                4L,
                "description",
                new UserDto(),
                time,
                List.of(itemDto));

        @Test
        void testItemRequestDto() throws Exception {
            JsonContent<ItemRequestDto> result = jsonDto.write(itemRequestDto);
            assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(4);
            assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
            assertThat(result).doesNotHaveEmptyJsonPathValue("$.requestor");
            assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-02-03T04:05:06");
            assertThat(result).doesNotHaveEmptyJsonPathValue("$.items");
        }

    }
