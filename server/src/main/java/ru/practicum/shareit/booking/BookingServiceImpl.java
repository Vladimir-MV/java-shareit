    package ru.practicum.shareit.booking;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.booking.dto.BookingDto;
    import ru.practicum.shareit.booking.dto.BookingMapper;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.exception.MessageFailedException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.EntityManager;
    import javax.persistence.PersistenceContext;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;


    @Slf4j
    @Service
    public class BookingServiceImpl implements BookingService{

        private BookingRepository bookingRepository;
        private ItemRepository itemRepository;
        private UserRepository userRepository;

        @PersistenceContext
        public EntityManager em;

        public BookingServiceImpl (BookingRepository bookingRepository, ItemRepository itemRepository,
                                   UserRepository userRepository) {
            this.bookingRepository = bookingRepository;
            this.itemRepository = itemRepository;
            this.userRepository = userRepository;
        }
        @Override
        public BookingDto createBooking (Optional<Long> idUser, Optional<BookingDto> bookingDtoIn)
                throws ValidationException, NoSuchElementException {
            User user = validationUser(idUser);
            Item item = validationItem(bookingDtoIn);
            if (user.getId().equals(item.getOwner().getId()))
                throw new NoSuchElementException("Бронирование запрошено собственником вещи. createBooking ()");
            if (!item.getAvailable())
                throw new ValidationException("Вещь недоступна для бронирования. createBooking ()");
            if (bookingDtoIn.get().getEnd().isBefore(bookingDtoIn.get().getStart()))
                throw new ValidationException  ("Дата окончания бронирования указана не верно! createBooking ()");
            LocalDateTime timeToday = LocalDateTime.now();
            if (bookingDtoIn.get().getEnd().isBefore(timeToday))
                throw new ValidationException  ("Дата окончания бронирования указана не верно! createBooking ()");
            if (bookingDtoIn.get().getStart().isBefore(timeToday))
                throw new ValidationException  ("Дата начала бронирования указана не верно! createBooking ()");
            Booking booking = BookingMapper.toBookingDtoIn(bookingDtoIn.get());
            booking.setItem(item);
            booking.setBooker(user);
            booking.setStatus(Status.WAITING);
            bookingRepository.save(booking);
            log.info("Забронирована вещь: {}", booking.getItem().getName());
            return BookingMapper.toBookingDto(booking);
        }

    public User validationUser (Optional<Long> idUser) throws NoSuchElementException {
        if (!idUser.isPresent()) throw new NoSuchElementException("Отсутствует id владельца! validationUser()");
        Optional<User> user = userRepository.findById(idUser.get());
        if (!user.isPresent())
            throw new NoSuchElementException("Указанный пользователь не найден! validationUser()");
        return user.get();
    }
        public Item validationItem (Optional<BookingDto> bookingDtoIn) throws NoSuchElementException {
            Optional<Item> item = itemRepository.findById(bookingDtoIn.get().getItemId());
            if (!item.isPresent()) throw new NoSuchElementException("Такой вещи нет! validationItem()");
            return item.get();
        }
    public Booking validBooking (Optional<Long> bookingId) throws ValidationException, NoSuchElementException {
        if (!bookingId.isPresent()) throw new ValidationException("Отсутствует id вещи! validationBooking()");
        Optional<Booking> booking = bookingRepository.findById(bookingId.get());
        if (!booking.isPresent())
                throw new NoSuchElementException("Указанный идентификатор брони не найден! validationBooking()");
        return booking.get() ;
        }
        @Override
        public BookingDto patchStatusBooking (Optional<Long> idUser, Optional<Long> bookingId, Boolean approved)
            throws ValidationException {
            User user = validationUser(idUser);
            Booking booking = validBooking(bookingId);
            Optional<Item> item = itemRepository.findById(booking.getItem().getId());
            if (!item.isPresent())
                throw new NoSuchElementException("Вещь не найдена! patchStatusBooking()");
            if (!item.get().getOwner().getId().equals(user.getId()))
                throw new NoSuchElementException("Владелец вещи не совпадает! patchStatusBooking()");
            if (booking.getStatus().equals(Status.APPROVED) && approved)
                throw new ValidationException(String.format("Статус уже был ранее изменен на %S patchStatusBooking()", approved));
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else if (!approved) {
                booking.setStatus(Status.REJECTED);
            } else {
                throw new ValidationException("Не верный параметр approved вещи! patchStatusBooking()");
            }
            bookingRepository.save(booking);
            log.info("Изменен статус брони {} , на {}", booking.getId(), booking.getStatus());
            return BookingMapper.toBookingDto(booking);
        }
        @Override
        public List<BookingDto> findBookingsAllById(Optional<Long> idUser) {
            User user = validationUser(idUser);
            List<Booking> list =  bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
            log.info("Вся бронь пользователя {} ", user.getName());
            return BookingMapper.toListBookingDto(list);
        }
        @Override
        public BookingDto findBookingById(Optional<Long> idUser, Optional<Long> bookingId)
                throws ValidationException, NoSuchElementException {
            User user = validationUser(idUser);
            Booking booking = validBooking(bookingId);
            if (booking.getBooker().getId().equals(user.getId()) ||
                    booking.getItem().getOwner().getId().equals(user.getId())) {
                log.info("Получена бронь номер {}", booking.getId());
                return BookingMapper.toBookingDto(booking);
            }
            throw new NoSuchElementException("Пользователю информация о бронировании не доступна, findBookingById");
        }
        @Override
        public List<BookingDto> findBookingsState(Optional<Long> idUser, Optional<Integer> from, Optional<Integer> size, String state)
                throws ValidationException, MessageFailedException {
            User user = validationUser(idUser);
            final Pageable pageable = FromSizeRequest.of(from.get(), size.get());
                switch (State.valueOf(state)) {
                    case ALL:
                        List <Booking> listBooking = bookingRepository
                                .findByBookingsListStateAll(idUser.get(), pageable).getContent();
                        log.info("Вся бронь пользователя {}, со статусом ALL", user.getName());
                        return BookingMapper.toListBookingDto(listBooking);
                    case CURRENT:
                        LocalDateTime time = LocalDateTime.now();
                        listBooking = bookingRepository
                                .findByBookingsListStateCurrent(idUser.get(), time, pageable).getContent();
                        log.info("Вся бронь пользователя {}, со статусом CURRENT", user.getName());
                        return BookingMapper.toListBookingDto(listBooking);
                    case PAST:
                        time = LocalDateTime.now();
                        listBooking = bookingRepository
                                .findByBookingsListStatePast(idUser.get(), time, pageable).getContent();
                        log.info("Вся бронь пользователя {}, со статусом PAST", user.getName());
                        return BookingMapper.toListBookingDto(listBooking);
                    case FUTURE:
                        time = LocalDateTime.now();
                        listBooking = bookingRepository
                                .findByBookingsListStateFuture(idUser.get(), time, pageable).getContent();
                        log.info("Вся бронь пользователя {}, со статусом FUTURE", user.getName());
                        for (Booking book: listBooking){
                            System.out.println(book.getId() + "  " + book.getStatus());
                        }
                        return BookingMapper.toListBookingDto(listBooking);
                    case WAITING:
                        listBooking = bookingRepository
                                .findByBookingsListStateWaiting(idUser.get(), pageable).getContent();
                        log.info("Вся бронь пользователя {}, со статусом WAITING", user.getName());
                        return BookingMapper.toListBookingDto(listBooking);
                    case REJECTED:
                        listBooking = bookingRepository
                                .findByBookingsListStateRejected(idUser.get(), pageable).getContent();
                        log.info("Вся бронь пользователя {}, со статусом REJECTED", user.getName());
                        return BookingMapper.toListBookingDto(listBooking);
                    default:
                        throw new MessageFailedException();
                }
        }
        @Override
        public List<BookingDto> findBookingsOwnerState (Optional<Long> idUser, Optional<Integer> from,
            Optional<Integer> size, String state) throws ValidationException, MessageFailedException {
            User user = validationUser(idUser);
            if (itemRepository.findByOwner_IdOrderById(idUser.get()).isEmpty())
                throw new ValidationException("У пользователя нет вещей! findBookingsOwnerState()");
            final Pageable pageable = FromSizeRequest.of(from.get(), size.get());
            switch (State.valueOf(state)) {
                case ALL:
                    List <Booking> listBooking = bookingRepository
                            .findByBookingsOwnerListStateAll(idUser.get(), pageable).getContent();
                    log.info("Вся бронь собственника {}, со статусом ALL", user.getName());
                    return BookingMapper.toListBookingDto(listBooking);
                case CURRENT:
                    LocalDateTime time = LocalDateTime.now();
                    listBooking = bookingRepository
                            .findByBookingsOwnerListStateCurrent(idUser.get(), time, pageable).getContent();
                    log.info("Вся бронь собственника {}, со статусом CURRENT", user.getName());
                    return BookingMapper.toListBookingDto(listBooking);
                case PAST:
                    time = LocalDateTime.now();
                    listBooking = bookingRepository
                            .findByBookingsOwnerListStatePast(idUser.get(), time, pageable).getContent();
                    log.info("Вся бронь собственника {}, со статусом PAST", user.getName());
                    return BookingMapper.toListBookingDto(listBooking);
                case FUTURE:
                    time = LocalDateTime.now();
                    listBooking = bookingRepository
                            .findByBookingsOwnerListStateFuture(idUser.get(), time, pageable).getContent();
                    log.info("Вся бронь собственника {}, со статусом FUTURE", user.getName());
                    for (Booking book: listBooking){
                        System.out.println(book.getId() + "  " + book.getStatus());
                    }
                    return BookingMapper.toListBookingDto(listBooking);
                case WAITING:
                    listBooking = bookingRepository
                            .findByBookingsOwnerListStateWaiting(idUser.get(), pageable).getContent();
                    log.info("Вся бронь собственника {}, со статусом WAITING", user.getName());
                    return BookingMapper.toListBookingDto(listBooking);
                case REJECTED:
                    listBooking = bookingRepository
                            .findByBookingsOwnerListStateRejected(idUser.get(), pageable).getContent();
                    log.info("Вся бронь собственника {}, со статусом REJECTED", user.getName());
                    return BookingMapper.toListBookingDto(listBooking);
                default:
                    throw new MessageFailedException();
            }
        }
    }
