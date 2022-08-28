    package ru.practicum.shareit.requests.model;

    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.*;
    import java.time.LocalDateTime;
    @Entity
    @Table(name = "requests")
    @Getter
    @Setter
    @NoArgsConstructor
    public class ItemRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String description;
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name = "users", joinColumns = @JoinColumn(name="id"))
        private User requestor;
        private LocalDateTime created;
    }
