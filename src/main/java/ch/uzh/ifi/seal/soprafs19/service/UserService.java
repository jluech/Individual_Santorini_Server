package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.controller.InexistingUser;
import ch.uzh.ifi.seal.soprafs19.controller.InvalidPassword;
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
        SimpleDateFormat creationFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");//define date format
        String creationStr = creationFormat.format(newUser.getCreationDate());//format Date type to String type
        newUser.setCreationDateStr(creationStr);

        SimpleDateFormat bdayFormat = new SimpleDateFormat("dd-MM-YYYY");//define date format
        String bdayStr = bdayFormat.format(newUser.getBirthdate());//format Date type to String type
        newUser.setBirthdateStr(bdayStr);

        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setToken(UUID.randomUUID().toString());
        newUser = userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        log.info("Registered user {} on {}", newUser.getUsername(), newUser.getCreationDateStr());
        return newUser;
    }

    public User updateUser(User updatingUser) {
        boolean hasChanged = false;
        long updateId = updatingUser.getId();
        User currentUser = getSingleUser(updateId);
        String upLastName = updatingUser.getLastName();
        String upFirstName = updatingUser.getFirstName();
        String upUsername = updatingUser.getUsername();
        String upPassword = updatingUser.getPassword();
        String upCurrentPw = updatingUser.getCurrentPassword();
        Date upBirthdate = updatingUser.getBirthdate();

        log.info("Updating user data for user {}", currentUser.getUsername());

        if((upLastName != null) && (!upLastName.equals(currentUser.getLastName()))) {
            String oldLastName = currentUser.getLastName();
            currentUser.setLastName(upLastName);
            String newLastName = currentUser.getLastName();
            log.info("updated lastname from {} to {}", oldLastName, newLastName);
            hasChanged = true;
        }
        if((upFirstName != null) && (!upFirstName.equals(currentUser.getFirstName()))) {
            String oldFirstName = currentUser.getFirstName();
            currentUser.setFirstName(upFirstName);
            String newFirstName = currentUser.getFirstName();
            log.info("updated firstname from {} to {}", oldFirstName, newFirstName);
            hasChanged = true;
        }
        if((upUsername != null) && (!upUsername.equals("")) &&(!upUsername.equals(currentUser.getUsername()))) {
            String oldUsername = currentUser.getUsername();
            currentUser.setUsername(upUsername);
            String newUsername = currentUser.getUsername();
            log.info("updated username from {} to {}", oldUsername, newUsername);
            hasChanged = true;
        }
        if((upPassword != null) && (!upPassword.equals("")) && (!upPassword.equals(currentUser.getPassword()))) {
                String oldPassword = currentUser.getPassword();
                currentUser.setPassword(upCurrentPw, upPassword);
                String newPassword = currentUser.getPassword();
                log.info("updated password successfully");
                hasChanged = true;
        }
        if((upBirthdate != null) && (!upBirthdate.equals(currentUser.getBirthdate()))) {//includes changing birthdayStr
            String oldBdayDate = currentUser.getBirthdateStr();
            currentUser.setBirthdate(upBirthdate);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
            String bdayStr = format.format(currentUser.getBirthdate());
            currentUser.setBirthdateStr(bdayStr);
            String newBdayDate = currentUser.getBirthdateStr();
            log.info("updated birthday date from {} to {}", oldBdayDate, newBdayDate);
            hasChanged = true;
        }
        if(!hasChanged) {
            throw new BadUpdateRequest();
        }
        userRepository.save(currentUser);
        return currentUser;//now is updated user
    }

    public void deleteUser(long id, User passwordUser) {
        User identifierUser = this.userRepository.findById(id);
        if(passwordUser.getPassword().equals(identifierUser.getPassword())) {
            this.userRepository.delete(identifierUser);
        } else {
            throw new InvalidPassword();
        }
    }

    public User loginUser(User identifierUser, User passwordUser) {
        if(passwordUser.getPassword().equals(identifierUser.getPassword())) {
            identifierUser.setStatus(UserStatus.ONLINE);
            userRepository.save(identifierUser);
        } else {
            throw new InvalidPassword();
        }
        return identifierUser;
    }

    public User logoutUser(User identifierUser) {
        identifierUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(identifierUser);
        return identifierUser;
    }

    public boolean validateUserToken(String token, long id) {
        User tokenUser = userRepository.findByToken(token);
        if(tokenUser == null) {
            throw new InexistingUser();
        }
        return tokenUser.getId().equals(id);
    }

    public boolean validateUserPassword(String password, long id) {
        User idUser = this.userRepository.findById(id);
        if(idUser == null) {
            throw new InexistingUser();
        }
        return idUser.getPassword().equals(password);
    }
}
