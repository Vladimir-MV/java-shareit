    package ru.practicum.shareit.user;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.exception.ConflictException;
    import ru.practicum.shareit.exception.ValidationException;
    import ru.practicum.shareit.user.dto.UserDto;
    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping(path = "/users")
    public class UserController {
        private UserService userService;

        @Autowired
        public UserController(UserService userService) {
            this.userService = userService;
        }

        @RequestMapping(value ="/",produces = "application/json")
        public String getURLValue(HttpServletRequest request){
            String test = request.getRequestURI();
            return test;
        }
        @GetMapping()
        protected List<UserDto> findAll() {
            return userService.findAllUserService();
        }

        @GetMapping("/{id}")
        protected UserDto findUserById(@PathVariable Optional<Long> id) {
            return userService.findUserByIdService(id);
        }

        @PatchMapping("/{id}")
        protected UserDto put(@PathVariable Optional<Long> id, @RequestBody UserDto userDto)
            throws ValidationException, ConflictException {
            return userService.patchUserService(userDto, id);
        }

        @DeleteMapping("/{id}")
        protected UserDto deleteUser(@PathVariable Optional<Long> id) {
            return userService.deleteUserService(id);
        }

        @PostMapping()
        protected UserDto create(@Valid @RequestBody UserDto userDto) throws ValidationException, ConflictException {
            return userService.createUserService(userDto);
        }
    }
