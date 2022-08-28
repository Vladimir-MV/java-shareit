    package ru.practicum.shareit.booking;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.booking.dto.*;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.exception.MessageFailedException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.user.model.User;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;
    @Slf4j
    @Service
    public class BookingServiceImpl implements BookingService{

        private BookingRepository bookingRepository;
        private ItemRepository itemRepository;
        private UserRepository userRepository;

        public BookingServiceImpl (BookingRepository bookingRepository, ItemRepository itemRepository,
                                   UserRepository userRepository) {
            this.bookingRepository = bookingRepository;
            this.itemRepository = itemRepository;
            this.userRepository = userRepository;
        }
        @Override
        public BookingDtoById createBooking (Optional<Long> idUser, Optional<BookingDtoIn> bookingDtoIn) throws ValidationException {
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
            Booking booking = BookingMapper.toBooking(bookingDtoIn.get());
            booking.setItem(item);
            booking.setBooker(user);
            booking.setStatus(Status.WAITING);
            bookingRepository.save(booking);
            log.info("Забронирована вещь: {}", booking.getItem().getName());
            return BookingMapper.toBookingDtoById(booking);
        }

    public User validationUser (Optional<Long> idUser) throws ValidationException {
        if (!idUser.isPresent()) throw new NoSuchElementException("Отсутствует id владельца! validationUser()");
        Optional<User> user = userRepository.findById(idUser.get());
        if (!user.isPresent())
            throw new NoSuchElementException("Указанный пользователь не найден! validationUser()");
        return user.get();
    }
        public Item validationItem (Optional<BookingDtoIn> bookingDtoIn) throws ValidationException {
            Optional<Item> item = itemRepository.findById(bookingDtoIn.get().getItemId());
            if (!item.isPresent()) throw new NoSuchElementException("Такой вещи нет! validationItem()");
            return item.get();
        }
    public Booking validBooking (Optional<Long> bookingId) throws ValidationException {
        if (!bookingId.isPresent()) throw new ValidationException("Отсутствует id вещи! validationBooking()");
        Optional<Booking> booking = bookingRepository.findById(bookingId.get());
        if (!booking.isPresent())
                throw new NoSuchElementException("Указанный идентификатор брони не найден! validationBooking()");
        return booking.get() ;
        }
        @Override
        public BookingDtoOut patchStatusBooking (Optional<Long> idUser, Optional<Long> bookingId, String approved)
            throws ValidationException {
            User user = validationUser(idUser);
            Booking booking = validBooking(bookingId);
            Optional<Item> item = itemRepository.findById(booking.getItem().getId());
            if (!item.isPresent())
                throw new NoSuchElementException("Вещь не найдена! patchStatusBooking()");
            if (!item.get().getOwner().getId().equals(user.getId()))
                throw new NoSuchElementException("Владелец вещи не совпадает! patchStatusBooking()");
            if (booking.getStatus().equals(Status.APPROVED) && (approved.equals("true")))
                throw new ValidationException(String.format("Статус уже был ранее изменен на %S patchStatusBooking()", approved));
            if (approved.equals("true")) {
                booking.setStatus(Status.APPROVED);
            } else if (approved.equals("false")) {
                booking.setStatus(Status.REJECTED);
            } else {
                throw new ValidationException("Не верный параметр approved вещи! patchStatusBooking()");
            }
            bookingRepository.save(booking);
            log.info("Изменен статус брони {} , на {}", booking.getId(), booking.getStatus());
            return BookingMapper.toBookingDto(booking);
        }
        @Override
        public List<BookingDtoOut> findBookingsAllById(Optional<Long> idUser) throws ValidationException {
            User user = validationUser(idUser);
            List<Booking> list =  bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
            log.info("Вся бронь пользователя {} ", user.getName());
            return BookingMapper.toListBookingDto(list);

        }
        @Override
        public BookingDtoOut findBookingById(Optional<Long> idUser, Optional<Long> bookingId) throws ValidationException {
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
        public List<BookingDtoOut> findBookingsState(Optional<Long> idUser, String state) throws ValidationException, MessageFailedException {
            User user = validationUser(idUser);
            List<Booking> list;
            switch (state){
                case "ALL":
                    list =  bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
                    List listAll = new ArrayList();
                    for (Booking booking: list){
                        if (booking.getStatus() != Status.REJECTED) listAll.add(booking);
                    }
                    log.info("Вся бронь пользователя {}, со статусом ALL", user.getName());
                    return BookingMapper.toListBookingDto(listAll);
                case "CURRENT":
                    list = bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
                    List listCurrent = new ArrayList();
                    LocalDateTime timeCurrent = LocalDateTime.now();
                    for (Booking booking: list){
                        if (timeCurrent.isBefore(booking.getEnd())
                                && timeCurrent.isAfter(booking.getStart())) listCurrent.add(booking);
                    }
                    log.info("Вся бронь пользователя {}, со статусом CURRENT", user.getName());
                    return BookingMapper.toListBookingDto(listCurrent);
                case "PAST":
                    list = bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
                    List listPast = new ArrayList();
                    LocalDateTime timePast = LocalDateTime.now();
                    for (Booking booking: list){
                        if (timePast.isAfter(booking.getEnd())) listPast.add(booking);
                    }
                    log.info("Вся бронь пользователя {}, со статусом PAST", user.getName());
                    return BookingMapper.toListBookingDto(listPast);
                case "FUTURE":
                    list = bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
                    List listFuture = new ArrayList();
                    LocalDateTime timeFuture = LocalDateTime.now();
                    for (Booking booking: list){
                        if (timeFuture.isBefore(booking.getStart())) listFuture.add(booking);
                    }
                    log.info("Вся бронь пользователя {}, со статусом FUTURE", user.getName());
                    return BookingMapper.toListBookingDto(listFuture);
                case "WAITING":
                    list = bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
                    List listWaiting = new ArrayList();
                    for (Booking booking: list){
                        if (booking.getStatus() == Status.WAITING) listWaiting.add(booking);
                    }
                    log.info("Вся бронь пользователя {}, со статусом WAITING", user.getName());
                    return BookingMapper.toListBookingDto(listWaiting);
                case "REJECTED":
                    list = bookingRepository.findByBooker_IdOrderByStartDesc(user.getId());
                    List listRejected = new ArrayList();
                    for (Booking booking: list){
                        if (booking.getStatus() == Status.REJECTED) listRejected.add(booking);
                    }
                    log.info("Вся бронь пользователя {}, со статусом REJECTED", user.getName());
                    return BookingMapper.toListBookingDto(listRejected);
                default:
                    throw new MessageFailedException ();
            }
        }
        @Override
        public List<BookingDtoOut> findBookingsOwnerState (Optional<Long> idUser, String state) throws ValidationException, MessageFailedException {
            User user = validationUser(idUser);
            List<Item> listItem = itemRepository.findByOwner_IdOrderById(idUser.get());
            if (listItem.isEmpty()) throw new ValidationException("У пользователя нет вещей! findBookingsOwnerState()");
            List<List<Booking>> list = new ArrayList<>();
            for (Item item: listItem) {
                Optional<List<Booking>> listBooking = bookingRepository.findByItem_IdOrderByStartDesc(item.getId());
                if (listBooking.isPresent()) {
                    list.add(listBooking.get());
                }
            }
            switch (state) {
                case "ALL":
                    List listAll = new ArrayList();
                    for (List<Booking> listItemBooking : list) {
                        for (Booking booking : listItemBooking) {
                        if (booking.getStatus() != Status.REJECTED) listAll.add(booking);
                        }
                    }
                    log.info("Вся бронь собственника {}, со статусом ALL", user.getName());
                    return BookingMapper.toListBookingDto(listAll);
                case "CURRENT":
                    List listCurrent = new ArrayList();
                    LocalDateTime timeCurrent = LocalDateTime.now();
                    for (List<Booking> listItemBooking : list) {
                        for (Booking booking : listItemBooking) {
                            if (timeCurrent.isBefore(booking.getEnd())
                                    && timeCurrent.isAfter(booking.getStart())) listCurrent.add(booking);
                        }
                    }
                    log.info("Вся бронь собственника {}, со статусом CURRENT", user.getName());
                    return BookingMapper.toListBookingDto(listCurrent);
                case "PAST":
                    List listPast = new ArrayList();
                    LocalDateTime timePast = LocalDateTime.now();
                    for (List<Booking> listItemBooking : list) {
                        for (Booking booking : listItemBooking) {
                            if (timePast.isAfter(booking.getEnd())) listPast.add(booking);
                        }
                    }
                    log.info("Вся бронь собственника {}, со статусом PAST", user.getName());
                    return BookingMapper.toListBookingDto(listPast);
                case "FUTURE":
                    List listFuture = new ArrayList();
                    LocalDateTime timeFuture = LocalDateTime.now();
                    for (List<Booking> listItemBooking : list) {
                        for (Booking booking : listItemBooking) {
                            if (timeFuture.isBefore(booking.getStart())) listFuture.add(booking);
                        }
                    }
                    log.info("Вся бронь собственника {}, со статусом FUTURE", user.getName());
                    return BookingMapper.toListBookingDto(listFuture);
                case "WAITING":
                    List listWaiting = new ArrayList();
                    for (List<Booking> listItemBooking : list) {
                        for (Booking booking : listItemBooking) {
                            if (booking.getStatus() == Status.WAITING) listWaiting.add(booking);
                        }
                    }
                    log.info("Вся бронь собственника {}, со статусом WAITING", user.getName());
                    return BookingMapper.toListBookingDto(listWaiting);
                case "REJECTED":
                    List listRejected = new ArrayList();
                    for (List<Booking> listItemBooking : list) {
                        for (Booking booking : listItemBooking) {
                            if (booking.getStatus() == Status.REJECTED) listRejected.add(booking);
                        }
                    }
                    log.info("Вся бронь собственника {}, со статусом REJECTED", user.getName());
                    return BookingMapper.toListBookingDto(listRejected);
                default:
                    throw new MessageFailedException();
            }
        }
    }
