    package ru.practicum.shareit.user;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import ru.practicum.shareit.user.dto.UserMapper;
    import ru.practicum.shareit.user.model.User;

    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping(path = "/users")
    public class UserController {
        private UserServiceImpl userServiceImpl;

        @Autowired
        public UserController(UserServiceImpl userServiceImpl) {
            this.userServiceImpl = userServiceImpl;
        }

        @RequestMapping(value ="/",produces = "application/json")
        public String getURLValue(HttpServletRequest request){
            String test = request.getRequestURI();
            return test;
        }
        @GetMapping()
        protected List<UserDto> findAll() {
            List<UserDto> list = new ArrayList<>();
            for (User user: userServiceImpl.findAllUserService()){
                list.add(UserMapper.toUserDto(user));
            }
            return list;
        }

        @GetMapping("/{id}")
        protected UserDto findUserById(@PathVariable Optional<Long> id) {
            return UserMapper.toUserDto(userServiceImpl.findUserByIdService(id));
        }

        @PatchMapping("/{id}")
        protected UserDto put(@PathVariable Optional<Long> id, @RequestBody UserDto userDto)
            throws ValidationException, ConflictException {
            return UserMapper.toUserDto(userServiceImpl.patchUserService(UserMapper.toUser(userDto), id));
        }

        @DeleteMapping("/{id}")
        protected UserDto deleteUser(@PathVariable Optional<Long> id) {
            return UserMapper.toUserDto(userServiceImpl.deleteUserService(id));
        }

        @PostMapping()
        protected UserDto create(@Valid @RequestBody UserDto userDto) throws ValidationException, ConflictException {
            return UserMapper.toUserDto(userServiceImpl.createUserService(UserMapper.toUser(userDto)));
        }
    }
