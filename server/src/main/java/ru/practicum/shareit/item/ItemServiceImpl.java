    package ru.practicum.shareit.item;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.booking.BookingRepository;
    import ru.practicum.shareit.booking.FromSizeRequest;
    import ru.practicum.shareit.booking.dto.BookingMapper;
    import ru.practicum.shareit.booking.model.Booking;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.dto.CommentDto;
    import ru.practicum.shareit.item.dto.ItemDto;
    import ru.practicum.shareit.item.dto.ItemDtoLastNext;
    import ru.practicum.shareit.item.dto.ItemMapper;
    import ru.practicum.shareit.item.model.Comment;
    import ru.practicum.shareit.item.model.Item;
    import ru.practicum.shareit.requests.ItemRequestRepository;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.user.model.User;
    import javax.persistence.EntityManager;
    import java.time.LocalDateTime;
    import java.util.*;

    @Slf4j
    @Service
    public class ItemServiceImpl implements ItemService {
        private ItemRepository itemRepository;
        private UserRepository userRepository;
        private CommentRepository commentRepository;
        private BookingRepository bookingRepository;
        private ItemRequestRepository itemRequestRepository;
        private EntityManager em;


        @Autowired
        public ItemServiceImpl (ItemRepository itemRepository, UserRepository userRepository,
            CommentRepository commentRepository, BookingRepository bookingRepository, ItemRequestRepository itemRequestRepository){
            this.itemRepository = itemRepository;
            this.userRepository = userRepository;
            this.commentRepository = commentRepository;
            this.bookingRepository = bookingRepository;
            this.itemRequestRepository = itemRequestRepository;
        }

        @Override
        public ItemDto createItem (Optional<Long> idUser, ItemDto itemDto) throws ValidationException {
            validationUser(idUser);
            Item item = ItemMapper.toItem(itemDto);
            if (itemDto.getRequestId() != null) item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).get());
            if (item.getName() == null || item.getName() == "")
                throw new ValidationException ("Не указаны данные - name для создания вещи! validationItem()");
            if (item.getDescription() == null || item.getDescription() == "")
                throw new ValidationException ("Не указаны данные - description для создания вещи! validationItem()");
            if (item.getAvailable() == null)
                throw new ValidationException ("Не указаны данные - available для создания вещи! validationItem()");
            item.setOwner(userRepository.findById(idUser.get()).get());
            itemRepository.save(item);
            log.info("Добавлена вещь: {}", item.getName());
            return ItemMapper.toItemDto(item);
        }
        public ItemDtoLastNext findLastNextBooking (Item item) {
                ItemDtoLastNext itemDtoLastNext = ItemMapper.toItemDtoLastNext(item);
                Optional<List<Booking>> listBooking = bookingRepository.findByItem_Id(item.getId());
                if ((listBooking.get().size() - 1) >= 0) {
                    itemDtoLastNext.setNextBooking(
                            BookingMapper.toBookingDto(listBooking.get().get(listBooking.get().size() - 1)));
                }
                if ((listBooking.get().size() - 2) >= 0) {
                    itemDtoLastNext.setLastBooking(
                            BookingMapper.toBookingDto(listBooking.get().get(listBooking.get().size() - 2)));
                }
           itemDtoLastNext.setComments(ItemMapper.toListItemDtoLastNext((commentRepository.findByItem_Id(item.getId()))));
           return itemDtoLastNext;

        }

        @Override
        public List<ItemDtoLastNext> findAllItemsOwner (Optional<Long> idUser, Optional<Integer> from, Optional<Integer> size)
                throws ValidationException {
            validationUser(idUser);
            final Pageable pageable = FromSizeRequest.of(from.get(), size.get());
            List<Item> listItem = itemRepository.findAllItemsOwner(idUser.get(), pageable).getContent();
            if (listItem.isEmpty()) throw new ValidationException("У пользователя нет вещей! findAllItemsOwner()");
            List<ItemDtoLastNext> list = new ArrayList<>();
            for (Item item : listItem) {
                list.add(findLastNextBooking(item));
            }
            log.info("Текущее количество вещей пользователя {}, в списке: {}", idUser.get(), list.size());
            return list;
        }

        @Override
        public ItemDtoLastNext findItemById (Optional<Long> idUser, Optional<Long> id)
                throws ValidationException, NoSuchElementException {
            validationUser(idUser);
            if (!id.isPresent())
                throw new NoSuchElementException("Не правильно задан id вещи! findItemById()");
            Item item = itemRepository.findById(id.get()).get();
            ItemDtoLastNext itemDto;
            if (item.getOwner().getId() == (idUser.get())) {
                itemDto = findLastNextBooking(item);
            } else {
                itemDto = ItemMapper.toItemDtoLastNext(item);
                itemDto.setComments(ItemMapper.toListCommentDto(commentRepository.findByItem_Id(item.getId())));
            }
            log.info("Просмотрена вещь: {}", item.getName());
            return itemDto;
    }
        @Override
        public ItemDto patchItem (Optional<Long> idUser, ItemDto itemDto, Optional<Long> id)
                throws ValidationException, NoSuchElementException {
            Item item = ItemMapper.toItem(itemDto);
            if (!id.isPresent())
                throw new NoSuchElementException("Отсутствует id вещи! patchItem()");
            validationUser(idUser);
            Item itemSt = itemRepository.findById(id.get()).get();
            if (itemSt.getOwner().getId() != (idUser.get()))
                throw new NoSuchElementException("Вещь не принадлежит этому владельцу! patchUser()");
            if (!(item.getName() == null || item.getName() == "")) itemSt.setName(item.getName());
            if (!(item.getDescription() == null || item.getDescription() == ""))
                itemSt.setDescription(item.getDescription());
            if (item.getAvailable() != null) itemSt.setAvailable(item.getAvailable());
            itemRepository.save(itemSt);
            log.info("Данные вещи: {} изменены.", itemSt.getName());
            return ItemMapper.toItemDto(itemSt);
        }
        @Override
        public ItemDto deleteItem(Optional<Long> idUser, Optional<Long> id)
                throws ValidationException, NoSuchElementException {
            validationUser(idUser);
            if (id.isPresent()) {
                Item item = itemRepository.findById(id.get()).get();
                if (item.getOwner().getId() != idUser.get())
                    throw new NoSuchElementException("Владелец вещи указан не верно! deleteItem()");
                itemRepository.delete(item);
                log.info("Вещь: {} удалена.", item.getName());
                return ItemMapper.toItemDto(item);
            }
            throw new NoSuchElementException("Id вещи не указан! deleteItemService()");
        }
        @Override
        public List<ItemDto> findItemSearch (Optional<Long> idUser, String text, Optional<Integer> from, Optional<Integer> size)
                throws ValidationException {
            validationUser(idUser);
            if (text == null || text.length() == 0) return Collections.emptyList();
            final Pageable pageable = PageRequest.of(from.get(), size.get());
            List<Item> listItem = itemRepository.searchListItem(text, pageable).getContent();
            if (listItem.isEmpty()) throw new ValidationException("По заданным параметрам вещи не найдены! findItemSearch ()");
        log.info("По заданным парамеррам найдены вещи! findItemSearch ()");
        return ItemMapper.toListItemDto(listItem);
        }
        public Optional<User> validationUser (Optional<Long> idUser) throws ValidationException, NoSuchElementException {
            if (!idUser.isPresent()) throw new ValidationException("Отсутствует id владельца! validationUser()");
            Optional<User> user = userRepository.findById(idUser.get());
            if (!user.isPresent())
                throw new NoSuchElementException("Указанный идентификатор пользователя не найден! validationUser()");
            return user;
        }
        public Optional<Item> validationItem (Optional<Long> itemId) throws ValidationException, NoSuchElementException {
            if (!itemId.isPresent()) throw new ValidationException("Отсутствует id вещи! validationComment()");
            Optional<Item> item = itemRepository.findById(itemId.get());
            if (!item.isPresent())
                throw new NoSuchElementException("Указанный идентификатор вещи не найден! validationComment()");
            return item;
        }
        @Override
        public CommentDto createComment (Optional<Long> idUser, Optional<Long> itemId, CommentDto commentDto) throws ValidationException {
            if (commentDto.getText().isEmpty())
                throw new ValidationException("Комментарий пустой! createComment()");
            Optional<User> user = validationUser(idUser);
            Optional<Item> item = validationItem(itemId);
            Optional<List<Booking>> bookings= bookingRepository.findByItem_IdAndBooker_id(itemId.get(), idUser.get());
            if (!bookings.isPresent())
                throw new ValidationException("Вещь не бронировалась! createComment()");
            LocalDateTime time = LocalDateTime.now();
            int count =0;
            for (Booking booking: bookings.get()) {
                if (booking.getStart().isAfter(time)) ++count;
            }
            if ((bookings.get().size()) == count)
                throw new ValidationException("Вещь забронирована, но использовалась! createComment()");
            Comment comment = ItemMapper.toComment(commentDto);
            comment.setItem(item.get());
            comment.setAuthor(user.get());
            comment.setCreated(time);
            commentRepository.save(comment);
            log.info("Создан комментарий номер {} ", comment.getId());
            return ItemMapper.toCommentDto(comment);
        }
    }
