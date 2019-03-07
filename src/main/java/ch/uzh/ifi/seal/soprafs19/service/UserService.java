package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getSingleUser(long id) {
        return this.userRepository.findById(id);
    }

    public User getSingleUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User createUser(User newUser) {
        SimpleDateFormat format = new SimpleDateFormat();
        String bdayStr = format.format(newUser.getBirthdate());//format Date type to String type
        newUser.setBirthdateStr(bdayStr);

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(User updatingUser) {
        User currentUser = getSingleUser(updatingUser.getId());
        //lastname, firstname, birthdate(Str), username, password

        if(!updatingUser.getLastName().equals(currentUser.getLastName())) {
            currentUser.setLastName(updatingUser.getLastName());
            //TODO: log change of old to new value
        }
        if(!updatingUser.getFirstName().equals(currentUser.getFirstName())) {
            currentUser.setFirstName(updatingUser.getFirstName());
            //TODO: log change of old to new value
        }
        if(!updatingUser.getUsername().equals(currentUser.getUsername())) {
            currentUser.setUsername(updatingUser.getUsername());
            //TODO: log change of old to new value
        }
        if(!updatingUser.getPassword().equals(currentUser.getPassword())) {
            currentUser.setPassword(updatingUser.getPassword());
            //TODO: log change of password w/o value, include errorReport value
        }
        if(!updatingUser.getBirthdate().equals(currentUser.getBirthdate())) {//include changing birthdayStr
            Date oldValue = currentUser.getBirthdate();

            currentUser.setBirthdate(updatingUser.getBirthdate());

            SimpleDateFormat format = new SimpleDateFormat();
            String bdayStr = format.format(currentUser.getBirthdate());
            currentUser.setBirthdateStr(bdayStr);

            //TODO: log change of old to new value
        }

        return currentUser;//now is updated user
    }
    //TODO: update existing user with new information
    //TODO: return updated user information

    public void deleteUser(long id) {
        User user = this.userRepository.findById(id);
        this.userRepository.delete(user);
    }
}
