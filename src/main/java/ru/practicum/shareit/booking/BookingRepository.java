    package ru.practicum.shareit.booking;


    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import ru.practicum.shareit.booking.model.Booking;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Optional;

    public interface BookingRepository extends JpaRepository<Booking, Long> {
        Optional<List<Booking>> findByItem_Id(Long ItemId);
        List<Booking> findByBooker_IdOrderByStartDesc(Long id);
        Optional<List<Booking>> findByItem_IdAndBooker_id(Long aLong, Long aLong1);

        @Query("select b from Booking b where b.booker.id = ?1 and b.status = 'REJECTED' order by b.start desc")
        List<Booking> searchListBookingRejected(Long id);

        @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = 'REJECTED' order by b.start desc")
        List<Booking> searchListBookingRejectedOwner(Long idUser);

        @Query("select b from Booking b where b.booker.id = ?1 and b.status = 'WAITING' order by b.start desc")
        List<Booking> searchListBookingWaiting(Long id);

        @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = 'WAITING' order by b.start desc")
        List<Booking> searchListBookingWaitingOwner(Long idUser);

        @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 and b.status <> 'REJECTED' order by b.start desc")
        List<Booking> searchListBookingPast(Long id, LocalDateTime time);

        @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < ?2 and b.status <> 'REJECTED' order by b.start desc")
        List<Booking> searchListBookingPastOwner(Long idUser, LocalDateTime time);

        @Query("select b from Booking b where b.booker.id = ?1 and ?2 between b.start and b.end order by b.start desc")
        List<Booking> searchListBookingCurrent(Long id, LocalDateTime time);

        @Query("select b from Booking b where b.item.owner.id = ?1 and ?2 between b.start and b.end order by b.start desc")
        List<Booking> searchListBookingCurrentOwner(Long idUser, LocalDateTime time);

        @Query("select b from Booking b where b.booker.id = ?1 and b.status <> 'REJECTED' order by b.start desc")
        List<Booking> searchListBookingALL(Long id);
        @Query("select b from Booking b where b.item.owner.id = ?1 and b.status <> 'REJECTED' order by b.start desc")
        List<Booking> searchListBookingALLOwner(Long idUser);

        @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start desc")
        List<Booking> searchListBookingFuture(Long id, LocalDateTime time);
        @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > ?2 order by b.start desc")
        List<Booking> searchListBookingFutureOwner(Long idUser, LocalDateTime time);

    }
