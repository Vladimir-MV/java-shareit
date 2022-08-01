package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.StatusBooking;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    @NotBlank
    private LocalDate start;
    @NotBlank
    private LocalDate end;
    @NotBlank
    private String item;
    @NotBlank
    private String booker;
    @NotBlank
    private StatusBooking status;
}
