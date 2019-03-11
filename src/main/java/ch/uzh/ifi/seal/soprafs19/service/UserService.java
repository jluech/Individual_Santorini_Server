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
        SimpleDateFormat creationFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");//define date format
        String creationStr = creationFormat.format(newUser.getCreationDate());//format Date type to String type
        newUser.setCreationDateStr(creationStr);

        SimpleDateFormat bdayFormat = new SimpleDateFormat("dd-MM-YYYY");//define date format
        String bdayStr = bdayFormat.format(newUser.getBirthdate());//format Date type to String type
        newUser.setBirthdateStr(bdayStr);

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        //TODO: remove online status=ONLINE and add in FE in login()
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        log.info("Registered user {} on {}", newUser.getUsername(), newUser.getCreationDateStr());
        return newUser;
    }

    public User updateUser(User updatingUser) {
        boolean hasChanged = false;

        long updateId = updatingUser.getId();
        log.info("extracted id {}", updateId);
        User currentUser = getSingleUser(updateId);
        log.info("found currentUser {} with id {}", currentUser.getUsername(), currentUser.getId());
        String upLastName = updatingUser.getLastName();
        String upFirstName = updatingUser.getFirstName();
        String upUsername = updatingUser.getUsername();
        String upPassword = updatingUser.getPassword();
        Date upBirthdate = updatingUser.getBirthdate();

        log.info("Updating user data for user {}", currentUser.getUsername());

        if((upLastName != null) && (!upLastName.equals(currentUser.getLastName()))) {
            String oldLastName = currentUser.getLastName();
            currentUser.setLastName(updatingUser.getLastName());
            String newLastName = currentUser.getLastName();
            log.info("updated lastname from {} to {}", oldLastName, newLastName);
            hasChanged = true;
            //TODO: log change of old to new value
        }
        if((upFirstName != null) && (!upFirstName.equals(currentUser.getFirstName()))) {
            String oldFirstName = currentUser.getFirstName();
            currentUser.setFirstName(updatingUser.getFirstName());
            String newFirstName = currentUser.getFirstName();
            log.info("updated firstname from {} to {}", oldFirstName, newFirstName);
            hasChanged = true;
            //TODO: log change of old to new value
        }
        if((upUsername != null) && (!upUsername.equals("")) &&(!upUsername.equals(currentUser.getUsername()))) {
            String oldUsername = currentUser.getUsername();
            currentUser.setUsername(updatingUser.getUsername());
            String newUsername = currentUser.getUsername();
            log.info("updated username from {} to {}", oldUsername, newUsername);
            hasChanged = true;
            //TODO: log change of old to new value
        }
        if((upPassword != null) && (!upPassword.equals("")) && (!upPassword.equals(currentUser.getPassword()))) {
            String oldPassword = currentUser.getPassword();
            currentUser.setPassword(updatingUser.getPassword());
            String newPassword = currentUser.getPassword();
            log.info("updated password from {} to {}", oldPassword, newPassword);
            hasChanged = true;
            //TODO: log change of password w/o value, include errorReport value
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
            //TODO: log change of old to new value
        }
        if(!hasChanged) {
            throw new BadUpdateRequest();
        }
        //TODO: save updated user in database
        userRepository.save(currentUser);
        return currentUser;//now is updated user
    }
    //TODO: update existing user with new information
    //TODO: return updated user information

    public void deleteUser(long id) {
        User user = this.userRepository.findById(id);
        this.userRepository.delete(user);
    }

    public void loginUser(User user) {
        user.setStatus(UserStatus.ONLINE);
    }

    public void logoutUser(User user) {
        user.setStatus(UserStatus.OFFLINE);
    }

    public boolean validateUserToken(String token, long id) {
        User tokenUser = this.userRepository.findByToken(token);
        User idUser = this.userRepository.findById(id);
        return idUser.getToken().equals(tokenUser.getToken());
    }

    public boolean validateUserPassword(String password, long id) {
        User idUser = this.userRepository.findById(id);
        return idUser.getPassword().equals(password);
    }
}
