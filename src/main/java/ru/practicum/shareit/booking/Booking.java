package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
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
