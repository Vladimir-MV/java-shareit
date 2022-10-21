    package ru.practicum.shareit.requests;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.item.ItemRepository;
    import ru.practicum.shareit.item.dto.ItemMapper;
    import ru.practicum.shareit.requests.dto.ItemRequestMapper;
    import ru.practicum.shareit.requests.model.ItemRequest;
    import ru.practicum.shareit.requests.dto.ItemRequestDto;
    import ru.practicum.shareit.user.UserRepository;
    import ru.practicum.shareit.user.model.User;
    import java.time.LocalDateTime;
    import java.util.Collections;
    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.Optional;

    @Slf4j
    @Service
    public class ItemRequestServiceImpl implements ItemRequestService{

        private ItemRequestRepository itemRequestRepository;
        private UserRepository userRepository;
        private ItemRepository itemRepository;


        @Autowired
        public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                      UserRepository userRepository, ItemRepository itemRepository){
            this.itemRequestRepository = itemRequestRepository;
            this.userRepository = userRepository;
            this.itemRepository = itemRepository;
        }

        public ItemRequestDto createItemRequest(Optional<Long> idUser, ItemRequestDto itemRequestDto) throws ValidationException {
            User user = validationUser(idUser);
            if (itemRequestDto.getDescription().isBlank())
                throw new ValidationException ("Запрос указан как null! createItemRequest()");
            ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
            itemRequest.setRequestor(user);
            itemRequest.setCreated(LocalDateTime.now());
            itemRequestRepository.save(itemRequest);
            log.info("Создан запрос id: {}", itemRequest.getId());
            return ItemRequestMapper.toItemRequestDto(itemRequest);
        }
        public User validationUser (Optional<Long> idUser) throws NoSuchElementException{
            if (!idUser.isPresent()) throw new NoSuchElementException("Отсутствует id владельца! validationUser()");
            Optional<User> user = userRepository.findById(idUser.get());
            if (!user.isPresent())
                throw new NoSuchElementException("Указанный пользователь не найден! validationUser()");
            return user.get();
        }
        public List<ItemRequestDto> findAllItemRequest(Optional<Long> idUser) {
            User user = validationUser(idUser);
            List<ItemRequestDto> list = ItemRequestMapper.toListItemRequestDto(
                    itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(user.getId()));
            for (ItemRequestDto itemRequestDto: list){
                itemRequestDto.setItems(ItemMapper.toListItemDto(
                        itemRepository.findByRequest_IdOrderByCreated(itemRequestDto.getId()).get()));
            }
            log.info("Получен список запросов пользователя: {}", user.getName());
            return list;
        }

        public ItemRequestDto findItemRequestById (Optional<Long> idUser, Optional<Long> id) throws NoSuchElementException {
            validationUser(idUser);
            if (!id.isPresent())
                throw new NoSuchElementException("Не правильно задан id запроса вещи! findItemRequestById()");
            Optional<ItemRequest> itemRequest = itemRequestRepository.findById(id.get());
            if (!itemRequest.isPresent())
                throw new NoSuchElementException("Такого запроса вещи не существует! findItemRequestById()");
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest.get());
            itemRequestDto.setItems(ItemMapper.toListItemDto(itemRepository.findByRequest_IdOrderByCreated(id.get()).get()));
            log.info("Получен запрос на вещь id: {}", itemRequestDto.getId());
            return itemRequestDto;
        }
        public List<ItemRequestDto> findItemRequestPageable (Optional<Long> idUser,
            Optional<Integer> from, Optional<Integer> size) {
            User user = validationUser(idUser);
            if (!from.isPresent() || !size.isPresent()) return Collections.emptyList();
            final Pageable pageable = PageRequest.of(from.get(), size.get());
            List<ItemRequest> requestList = itemRequestRepository
                            .findByItemRequestListRequestor(idUser.get(), pageable).getContent();
            List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.toListItemRequestDto(requestList);
            for (ItemRequestDto itemRequest: itemRequestDtoList) {
                if (itemRepository.findByRequest_IdOrderByCreated(itemRequest.getId()).isPresent())
                    itemRequest.setItems(ItemMapper.toListItemDto(itemRepository
                            .findByRequest_IdOrderByCreated(itemRequest.getId()).get()));
            }
            log.info("Пользователь {} получил список запросов.", user.getName());
            return itemRequestDtoList;
        }

    }
