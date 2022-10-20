    package ru.practicum.shareit.user;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
    import ru.practicum.shareit.user.dto.UserDto;
    import javax.validation.Valid;

    @Controller
    @RequiredArgsConstructor
    @Slf4j
    @RequestMapping(path = "/users")
    public class UserController {
        UserClient userClient;
        @Autowired
        public UserController(UserClient userClient) {
            this.userClient = userClient;
        }

        @GetMapping()
        public ResponseEntity<Object> findAllUserGateway() {
            log.info("find all users");
            return userClient.findAllUser();
        }

        @GetMapping("/{userId}")
        public ResponseEntity<Object> findUserByIdGateway(@PathVariable long userId) {
            log.info("findUserByIdGateway, userId={}", userId);
            return userClient.findUserById(userId);
        }

        @PatchMapping("/{userId}")
        public ResponseEntity<Object> putUserGateway(@PathVariable long userId, @RequestBody UserDto userDto) {
            log.info("put user, userDto {}, userId={}", userDto, userId);
            return userClient.patchUser(userId, userDto);
        }
        @DeleteMapping("/{userId}")
        public ResponseEntity<Object> deleteUserGateway(@PathVariable long userId) {
            log.info("deleteUser, userId={}", userId);
            return userClient.deleteUser(userId);
        }

        @PostMapping()
        public ResponseEntity<Object> createUserGateway(@Valid @RequestBody UserDto userDto) {
            log.info("create user, userDto {}", userDto);
            return userClient.createUser(userDto);
        }
    }
