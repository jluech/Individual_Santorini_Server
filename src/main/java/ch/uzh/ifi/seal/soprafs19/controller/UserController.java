package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; //includes all mappings


@RestController
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/validate/token/{token}/{id}")
    @ResponseStatus(HttpStatus.OK)
    boolean validateToken(@PathVariable String token, @PathVariable long id) {
        log.info(String.format("validating token for user %s with %s", id, token));
        if(token==null || !this.service.validateUserToken(token, id)) {
            throw new InexistingUser();
        } else {
            return true;
        }
    }

    @GetMapping("/validate/password/{password}/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    boolean validatePassword(@PathVariable String password, @PathVariable long id) {
        log.info(String.format("validating password for user %s", id));
        if(!this.service.validateUserPassword(password, id)) {
            throw new InvalidPassword();
        } else {
            return true;
        }
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

    @CrossOrigin
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    User updateUser(@PathVariable long id, @RequestBody User updatedUser) {
        User currentUser = this.service.getSingleUser(id);
        if(currentUser == null) {
            throw new InexistingUser();
        } else {
            return this.service.updateUser(updatedUser);
        }
    }

    @CrossOrigin
    @PutMapping("/users/login/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void loginUser(@PathVariable String username) {
        User currentUser = this.service.getSingleUser(username);
        if(currentUser == null) {
            throw new InexistingUser();
        } else {
            this.service.loginUser(currentUser);
        }
    }

    @CrossOrigin
    @PutMapping("/users/logout/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void logoutUser(@PathVariable String username) {
        User currentUser = this.service.getSingleUser(username);
        if(currentUser == null) {
            throw new InexistingUser();
        } else {
            this.service.logoutUser(currentUser);
        }
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUser(@PathVariable long id) { service.deleteUser(id); }
}
