package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;//added again
import org.springframework.web.bind.annotation.PostMapping;//added again
import org.springframework.web.bind.annotation.RequestBody;//added again
import org.springframework.web.bind.annotation.RestController;//added again
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @GetMapping("/users/username/{username}")
    User getUserUsername(@PathVariable String username) { return service.getSingleUser(username); }

    @GetMapping("/users/id/{id}")
    User getUserId(@PathVariable long id) { return service.getSingleUser(id); }

    @DeleteMapping("/users/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUser(@PathVariable long id) { service.deleteUser(id); }

    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }
}
