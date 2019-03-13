package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;


/**
 * Test class for the UserResource REST resource.
 *
 * @see UserController
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserControllerTest {

    private final Date today = new Date();

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    public void validateToken() {
        User testControllerValidateTokenUser = new User();

    }

    @Test
    public void validatePassword() {
        User testControllerValidatePasswordUser = new User();
        testControllerValidatePasswordUser.setUsername("testControllerValidatePasswordUsername");
        testControllerValidatePasswordUser.setPassword("testControllerValidatePasswordPassword");
        testControllerValidatePasswordUser.setCreationDate(today);
        testControllerValidatePasswordUser.setBirthdate(today);

        userController.createUser(testControllerValidatePasswordUser);
        String testControllerValidatePasswordUserUsername = testControllerValidatePasswordUser.getUsername();
        User createdControllerValidatePasswordUser = userRepository.findByUsername(testControllerValidatePasswordUserUsername);
        String createdControllerValidatePasswordUserPassword = createdControllerValidatePasswordUser.getPassword();
        long createdControllerValidatePasswordUserId = createdControllerValidatePasswordUser.getId();

        Assert.assertTrue(userController.validatePassword(createdControllerValidatePasswordUserPassword, createdControllerValidatePasswordUserId));
        Assert.assertTrue(userController.validatePassword(createdControllerValidatePasswordUser.getPassword(), createdControllerValidatePasswordUser.getId()));
    }

    @Test
    public void all() {
    }

    @Test
    public void createUser() {
        User testControllerCreateUser = new User();
        testControllerCreateUser.setFirstName("testControllerCreateFirstName");
        testControllerCreateUser.setLastName("testControllerCreateLastName");
        testControllerCreateUser.setUsername("testControllerCreateUsername");
        testControllerCreateUser.setPassword("testControllerCreatePassword");
        testControllerCreateUser.setBirthdate(today);
        testControllerCreateUser.setCreationDate(today);

        String createdControllerCreateUserUrl = userController.createUser(testControllerCreateUser);
        Assert.assertTrue(createdControllerCreateUserUrl.startsWith("/users/"));

        String createdControllerCreateUserIdString = createdControllerCreateUserUrl.substring(7);
        Assert.assertFalse(createdControllerCreateUserIdString.isEmpty());
        Assert.assertFalse(createdControllerCreateUserIdString.isBlank());
        Assert.assertTrue(isNumeric(createdControllerCreateUserIdString));

        long createdControllerCreateUserId = Long.parseLong(createdControllerCreateUserIdString);
        User createdControllerCreateUser = userRepository.findById(createdControllerCreateUserId);

        //TODO: check for response status

        Assert.assertEquals(testControllerCreateUser.getUsername(), createdControllerCreateUser.getUsername());
        Assert.assertEquals(testControllerCreateUser.getFirstName(), createdControllerCreateUser.getFirstName());
        Assert.assertEquals(testControllerCreateUser.getLastName(), createdControllerCreateUser.getLastName());
        Assert.assertEquals(testControllerCreateUser.getPassword(), createdControllerCreateUser.getPassword());
        Assert.assertEquals(testControllerCreateUser.getBirthdate(), createdControllerCreateUser.getBirthdate());
        Assert.assertEquals(testControllerCreateUser.getCreationDate(), createdControllerCreateUser.getCreationDate());
        Assert.assertNotNull(createdControllerCreateUser.getId());
        Assert.assertNotNull(createdControllerCreateUser.getToken());
    }

    @Test
    public void getUserId() {
        User testControllerGetIdUser = new User();
        testControllerGetIdUser.setUsername("testControllerGetIdUsername");
        testControllerGetIdUser.setPassword("testControllerGetIdPassword");
        testControllerGetIdUser.setBirthdate(today);
        testControllerGetIdUser.setCreationDate(today);

        userController.createUser(testControllerGetIdUser);
        String testControllerGetIdUserUsername = testControllerGetIdUser.getUsername();
        User createdControllerGetIdUser = userRepository.findByUsername(testControllerGetIdUserUsername);
        Assert.assertEquals(testControllerGetIdUser.getUsername(), createdControllerGetIdUser.getUsername());
        Assert.assertEquals(testControllerGetIdUser.getPassword(), createdControllerGetIdUser.getPassword());
        Assert.assertEquals(testControllerGetIdUser.getBirthdate(), createdControllerGetIdUser.getBirthdate());
        Assert.assertEquals(testControllerGetIdUser.getCreationDate(), createdControllerGetIdUser.getCreationDate());
        long createdControllerGetIdUserId = createdControllerGetIdUser.getId();

        User testControllerGetIdFoundUser = userController.getUserId(createdControllerGetIdUserId);
        Assert.assertEquals(testControllerGetIdUser, testControllerGetIdFoundUser);
        Assert.assertEquals(testControllerGetIdUser.getUsername(), testControllerGetIdFoundUser.getUsername());
    }

    @Test
    public void getUserUsername() {
        User testControllerGetUsernameUser = new User();
        testControllerGetUsernameUser.setUsername("testControllerGetUsernameUsername");
        testControllerGetUsernameUser.setPassword("testControllerGetUsernamePassword");
        testControllerGetUsernameUser.setBirthdate(today);
        testControllerGetUsernameUser.setCreationDate(today);

        userController.createUser(testControllerGetUsernameUser);
        String testControllerGetUsernameUserUsername = testControllerGetUsernameUser.getUsername();
        User createdControllerGetUsernameUser = userRepository.findByUsername(testControllerGetUsernameUserUsername);
        Assert.assertEquals(testControllerGetUsernameUser.getUsername(), createdControllerGetUsernameUser.getUsername());
        Assert.assertEquals(testControllerGetUsernameUser.getPassword(), createdControllerGetUsernameUser.getPassword());
        Assert.assertEquals(testControllerGetUsernameUser.getBirthdate(), createdControllerGetUsernameUser.getBirthdate());
        Assert.assertEquals(testControllerGetUsernameUser.getCreationDate(), createdControllerGetUsernameUser.getCreationDate());
        String createdControllerGetUsernameUserUsername = createdControllerGetUsernameUser.getUsername();

        User testControllerGetUsernameFoundUser = userController.getUserUsername(createdControllerGetUsernameUserUsername);
        Assert.assertEquals(testControllerGetUsernameUser, testControllerGetUsernameFoundUser);
        Assert.assertEquals(testControllerGetUsernameUser.getUsername(), testControllerGetUsernameFoundUser.getUsername());
    }

    @Test
    public void updateUser() {
        User testControllerUpdateUser = new User();
        testControllerUpdateUser.setFirstName("testControllerUpdateFirstName");
        testControllerUpdateUser.setLastName("testControllerUpdateLastName");
        testControllerUpdateUser.setUsername("testControllerUpdateUsername");
        testControllerUpdateUser.setPassword("testControllerUpdatePassword");
        testControllerUpdateUser.setBirthdate(today);
        testControllerUpdateUser.setCreationDate(today);

        userController.createUser(testControllerUpdateUser);
        String testControllerUpdateUserUsername = testControllerUpdateUser.getUsername();
        User createdControllerUpdateUserOriginal = userRepository.findByUsername(testControllerUpdateUserUsername);
        long createdControllerUpdateUserOriginalId = createdControllerUpdateUserOriginal.getId();

        User testControllerUpdateUserUpdated = new User();
        testControllerUpdateUserUpdated.setId(createdControllerUpdateUserOriginalId);
        testControllerUpdateUserUpdated.setFirstName("testControllerUpdatedFirst");
        testControllerUpdateUserUpdated.setLastName("testControllerUpdatedLast");
        testControllerUpdateUserUpdated.setUsername("testControllerUpdatedUname");
        testControllerUpdateUserUpdated.setPassword("testControllerUpdatedPass");

        userController.updateUser(createdControllerUpdateUserOriginalId, testControllerUpdateUserUpdated);//updating via id

        Assert.assertEquals(createdControllerUpdateUserOriginalId, (long)createdControllerUpdateUserOriginal.getId()); //casting for unambiguous function call
        Assert.assertEquals(testControllerUpdateUserUpdated.getId(), createdControllerUpdateUserOriginal.getId());
        Assert.assertEquals(testControllerUpdateUserUpdated.getFirstName(), createdControllerUpdateUserOriginal.getFirstName());
        Assert.assertEquals(testControllerUpdateUserUpdated.getLastName(), createdControllerUpdateUserOriginal.getLastName());
        Assert.assertEquals(testControllerUpdateUserUpdated.getUsername(), createdControllerUpdateUserOriginal.getUsername());
        Assert.assertEquals(testControllerUpdateUserUpdated.getPassword(), createdControllerUpdateUserOriginal.getPassword());
    }

    @Test
    public void loginUser() {
    }

    @Test
    public void logoutUser() {
    }

    @Test
    public void deleteUser() {
    }
}
