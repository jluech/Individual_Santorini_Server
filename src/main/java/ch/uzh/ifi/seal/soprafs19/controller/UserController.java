package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; //includes all mappings

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

    @PostMapping("/users")
    @ResponseStatus (HttpStatus.CREATED)
    User createUser(@RequestBody User newUser) {
        //String createUser(@RequestBody User newUser) {
        String username = newUser.getUsername();
        if(this.service.getSingleUser(username) != null) {
            throw new ExistingUser();
        } else {
            return this.service.createUser(newUser);
        }
    }
    //TODO: Return location: url<string> if successful;
    //TODO: return reason<string> otherwise //=> in throw included?

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getUserId(@PathVariable long id) {
        User foundUser = this.service.getSingleUser(id);
        if(foundUser == null) {
            throw new InexistingUser();
        } else {
            return foundUser;
        }
    }
    //TODO: exclude password in return id<long>, username<string>, creation_date<date>, logged_in<boolean>, birthday<date>

    @GetMapping("/users/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    User getUserUsername(@PathVariable String username) {
        User foundUser = this.service.getSingleUser(username);
        if(foundUser == null) {
            throw new InexistingUser();
        } else {
            return foundUser;
        }
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    User updateUser(@PathVariable long id, @RequestBody User updatedUser) {
        User currentUser =this.service.getSingleUser(id);
        if(currentUser == null) {
            throw new InexistingUser();
        } else {
            return this.service.updateUser(updatedUser);
        }
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUser(@PathVariable long id) { service.deleteUser(id); }

}
