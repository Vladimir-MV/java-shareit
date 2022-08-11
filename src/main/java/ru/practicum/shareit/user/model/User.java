    package ru.practicum.shareit.user.model;

    import lombok.Getter;
    import lombok.Setter;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    import javax.validation.constraints.Email;
    import javax.validation.constraints.NotBlank;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
        private Long id;
        @NotBlank
        private String name;
        @NotBlank
        @Email
        private String email;
    }
