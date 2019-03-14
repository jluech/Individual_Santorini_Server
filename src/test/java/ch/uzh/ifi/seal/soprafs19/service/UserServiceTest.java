package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {

    private final Date today = new Date();

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testCreateUsername"));

        User testCreateUser = new User();
        testCreateUser.setFirstName("testCreateFirstName");
        testCreateUser.setLastName("testCreateLastName");
        testCreateUser.setUsername("testCreateUsername");
        testCreateUser.setPassword("", "testCreatePassword");
        testCreateUser.setBirthdate(today);
        testCreateUser.setCreationDate(today);

        SimpleDateFormat creationFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");//define date format
        SimpleDateFormat bdayFormat = new SimpleDateFormat("dd-MM-YYYY");//define date format

        User createdCreateUser = userService.createUser(testCreateUser);

        String creationStr = creationFormat.format(createdCreateUser.getCreationDate());//format Date type to String type
        String bdayStr = bdayFormat.format(createdCreateUser.getBirthdate());//format Date type to String type

        Assert.assertEquals(creationStr, createdCreateUser.getCreationDateStr());
        Assert.assertEquals(bdayStr, createdCreateUser.getBirthdateStr());
        Assert.assertNotNull(createdCreateUser.getToken());
        Assert.assertEquals(createdCreateUser.getStatus(),UserStatus.OFFLINE);

        long testCreateUserId = createdCreateUser.getId();

        Assert.assertEquals(createdCreateUser, userRepository.findByToken(createdCreateUser.getToken()));
        Assert.assertEquals(createdCreateUser, userRepository.findByUsername(createdCreateUser.getUsername()));
        Assert.assertEquals(createdCreateUser, userRepository.findById(testCreateUserId));

        userService.deleteUser(createdCreateUser.getId(), createdCreateUser.getPassword()); //cleanup
    }

    @Test
    public void getUserId() {
        Assert.assertNull(userRepository.findByUsername("testGetIdUsername"));

        User testGetIdUser = new User();
        testGetIdUser.setUsername("testGetIdUsername");
        testGetIdUser.setPassword("", "testGetIdPassword");
        testGetIdUser.setBirthdate(today);
        testGetIdUser.setCreationDate(today);

        User createdGetUserIdUser = userService.createUser(testGetIdUser);
        long testGetUserId = createdGetUserIdUser.getId();

        Assert.assertEquals(createdGetUserIdUser, userRepository.findById(testGetUserId));
        Assert.assertEquals(testGetUserId, (long)userRepository.findById(testGetUserId).getId());

        Assert.assertEquals(createdGetUserIdUser, userService.getSingleUser(testGetUserId));
        Assert.assertEquals(createdGetUserIdUser, userService.getSingleUser(createdGetUserIdUser.getId()));

        userService.deleteUser(createdGetUserIdUser.getId(), createdGetUserIdUser.getPassword()); //cleanup
    }

    @Test
    public void getUserUsername() {
        Assert.assertNull(userRepository.findByUsername("testGetUsernameUsername"));

        User testGetUsernameUser = new User();
        testGetUsernameUser.setUsername("testGetUsernameUsername");
        testGetUsernameUser.setPassword("", "testGetUsernamePassword");
        testGetUsernameUser.setBirthdate(today);
        testGetUsernameUser.setCreationDate(today);

        User createdGetUserUsernameUser = userService.createUser(testGetUsernameUser);

        Assert.assertEquals(createdGetUserUsernameUser, userRepository.findByUsername("testGetUsernameUsername"));
        Assert.assertEquals(createdGetUserUsernameUser, userRepository.findByUsername(createdGetUserUsernameUser.getUsername()));
        Assert.assertEquals("testGetUsernameUsername", userRepository.findByUsername("testGetUsernameUsername").getUsername());
        Assert.assertEquals(createdGetUserUsernameUser.getUsername(), userRepository.findByUsername(createdGetUserUsernameUser.getUsername()).getUsername());

        Assert.assertEquals(createdGetUserUsernameUser, userService.getSingleUser("testGetUsernameUsername"));
        Assert.assertEquals(createdGetUserUsernameUser, userService.getSingleUser(createdGetUserUsernameUser.getUsername()));

        userService.deleteUser(createdGetUserUsernameUser.getId(), createdGetUserUsernameUser.getPassword()); //cleanup
    }

    @Test
    public void updateUser() {
        Assert.assertNull(userRepository.findByUsername("testUpdateUsername"));

        User testUpdateUser = new User();
        testUpdateUser.setFirstName("testUpdateFirstName");
        testUpdateUser.setLastName("testUpdateLastName");
        testUpdateUser.setUsername("testUpdateUsername");
        testUpdateUser.setCurrentPassword("testUpdatePassword"); //needs to be assigned before password because of validation for security reasons
        testUpdateUser.setPassword(testUpdateUser.getCurrentPassword(), "testUpdatePassword");
        testUpdateUser.setBirthdate(today);
        testUpdateUser.setCreationDate(today);

        User createdUpdateUserOriginal = userService.createUser(testUpdateUser);
        long createdUpdateUserOriginalId = createdUpdateUserOriginal.getId();

        User testNowUpdatedUser = new User();
        testNowUpdatedUser.setId(createdUpdateUserOriginalId);
        testNowUpdatedUser.setFirstName("testUpdatedFirst");
        testNowUpdatedUser.setLastName("testUpdatedLast");
        testNowUpdatedUser.setUsername("testUpdatedUname");
        testNowUpdatedUser.setPassword("", "testUpdatedPass");
        testNowUpdatedUser.setCurrentPassword(testUpdateUser.getPassword());
        testNowUpdatedUser.setBirthdate(today);
        testNowUpdatedUser.setCreationDate(today);

        Assert.assertEquals(createdUpdateUserOriginal.getId(), testNowUpdatedUser.getId());
        createdUpdateUserOriginal = userService.updateUser(testNowUpdatedUser); //updating via id

        Assert.assertEquals(createdUpdateUserOriginalId, (long)createdUpdateUserOriginal.getId()); //casting for unambiguous function call
        Assert.assertEquals("testUpdatedFirst", createdUpdateUserOriginal.getFirstName());
        Assert.assertEquals("testUpdatedLast", createdUpdateUserOriginal.getLastName());
        Assert.assertEquals("testUpdatedUname", createdUpdateUserOriginal.getUsername());
        Assert.assertEquals("testUpdatedPass", createdUpdateUserOriginal.getPassword());
        Assert.assertEquals(testNowUpdatedUser.getId(), createdUpdateUserOriginal.getId());
        Assert.assertEquals(testNowUpdatedUser.getFirstName(), createdUpdateUserOriginal.getFirstName());
        Assert.assertEquals(testNowUpdatedUser.getLastName(), createdUpdateUserOriginal.getLastName());
        Assert.assertEquals(testNowUpdatedUser.getUsername(), createdUpdateUserOriginal.getUsername());
        Assert.assertEquals(testNowUpdatedUser.getPassword(), createdUpdateUserOriginal.getPassword());

        userService.deleteUser(createdUpdateUserOriginal.getId(), createdUpdateUserOriginal.getPassword()); //cleanup
    }

    @Test(expected = BadUpdateRequest.class)
    public void updateUserBad() {
        Assert.assertNull(userRepository.findByUsername("testUpdateUsernameBad"));

        User testUpdateUser = new User();
        testUpdateUser.setFirstName("testUpdateFirstNameBad");
        testUpdateUser.setLastName("testUpdateLastNameBad");
        testUpdateUser.setUsername("testUpdateUsernameBad");
        testUpdateUser.setCurrentPassword("testUpdatePasswordBad"); //needs to be assigned before password because of validation for security reasons
        testUpdateUser.setPassword(testUpdateUser.getCurrentPassword(), "testUpdatePasswordBad");
        testUpdateUser.setBirthdate(today);
        testUpdateUser.setCreationDate(today);

        User createdUpdateUserOriginal = userService.createUser(testUpdateUser);
        long createdUpdateUserOriginalId = createdUpdateUserOriginal.getId();

        User testNowUpdatedUser = new User();
        testNowUpdatedUser.setId(createdUpdateUserOriginalId);
        testNowUpdatedUser.setFirstName(testUpdateUser.getFirstName());
        testNowUpdatedUser.setLastName(testUpdateUser.getLastName());
        testNowUpdatedUser.setUsername(testUpdateUser.getUsername());
        testNowUpdatedUser.setPassword("", testUpdateUser.getPassword());
        testNowUpdatedUser.setCurrentPassword(testUpdateUser.getPassword());
        testNowUpdatedUser.setBirthdate(today);
        testNowUpdatedUser.setCreationDate(today);

        Assert.assertEquals(createdUpdateUserOriginal.getId(), testNowUpdatedUser.getId());
        createdUpdateUserOriginal = userService.updateUser(testNowUpdatedUser); //updating via id

        Assert.assertEquals(createdUpdateUserOriginalId, (long)createdUpdateUserOriginal.getId()); //casting for unambiguous function call
        Assert.assertEquals("testUpdateFirstNameBad", createdUpdateUserOriginal.getFirstName());
        Assert.assertEquals("testUpdateLastNameBad", createdUpdateUserOriginal.getLastName());
        Assert.assertEquals("testUpdateUsernameBad", createdUpdateUserOriginal.getUsername());
        Assert.assertEquals("testUpdatePasswordBad", createdUpdateUserOriginal.getPassword());
        Assert.assertEquals(testNowUpdatedUser.getId(), createdUpdateUserOriginal.getId());
        Assert.assertEquals(testNowUpdatedUser.getFirstName(), createdUpdateUserOriginal.getFirstName());
        Assert.assertEquals(testNowUpdatedUser.getLastName(), createdUpdateUserOriginal.getLastName());
        Assert.assertEquals(testNowUpdatedUser.getUsername(), createdUpdateUserOriginal.getUsername());
        Assert.assertEquals(testNowUpdatedUser.getPassword(), createdUpdateUserOriginal.getPassword());

        userService.deleteUser(createdUpdateUserOriginal.getId(), createdUpdateUserOriginal.getPassword()); //cleanup
    }

    @Test
    public void loginUser() {
        Assert.assertNull(userRepository.findByUsername("testLoginUsername"));

        User testLoginUser = new User();
        testLoginUser.setUsername("testLoginUsername");
        testLoginUser.setPassword("", "testLoginPassword");
        testLoginUser.setBirthdate(today);
        testLoginUser.setCreationDate(today);

        User createdLoginUser = userService.createUser(testLoginUser);

        Assert.assertEquals(UserStatus.OFFLINE, createdLoginUser.getStatus());
        createdLoginUser = userService.loginUser(createdLoginUser, createdLoginUser.getPassword());
        Assert.assertEquals(UserStatus.ONLINE, createdLoginUser.getStatus());

        userService.deleteUser(createdLoginUser.getId(), createdLoginUser.getPassword()); //cleanup
    }

    @Test
    public void logoutUser() {
        Assert.assertNull(userRepository.findByUsername("testLogoutUsername"));

        User testLogoutUser = new User();
        testLogoutUser.setUsername("testLogoutUsername");
        testLogoutUser.setCurrentPassword("testLogoutPassword");
        testLogoutUser.setPassword(testLogoutUser.getCurrentPassword(), "testLogoutPassword");
        testLogoutUser.setBirthdate(today);
        testLogoutUser.setCreationDate(today);

        User createdLogoutUser = userService.createUser(testLogoutUser);
        userService.loginUser(createdLogoutUser, createdLogoutUser.getPassword()); //login first in order to be able to logout

        Assert.assertEquals(UserStatus.ONLINE, createdLogoutUser.getStatus());
        createdLogoutUser = userService.logoutUser(createdLogoutUser);
        Assert.assertEquals(UserStatus.OFFLINE, createdLogoutUser.getStatus());

        userService.deleteUser(createdLogoutUser.getId(), createdLogoutUser.getPassword()); //cleanup
    }

    @Test
    public void deleteUser() {
        Assert.assertNull(userRepository.findByUsername("testDeleteUsername"));

        User testDeleteUser = new User();
        testDeleteUser.setUsername("testDeleteUsername");
        testDeleteUser.setCurrentPassword("testDeletePassword");
        testDeleteUser.setPassword(testDeleteUser.getCurrentPassword(), "testDeletePassword");
        testDeleteUser.setBirthdate(today);
        testDeleteUser.setCreationDate(today);

        User createdDeleteUser = userService.createUser(testDeleteUser);
        long testDeleteId = createdDeleteUser.getId();
        String testDeleteToken = createdDeleteUser.getToken();

        Assert.assertEquals(createdDeleteUser, userRepository.findByUsername("testDeleteUsername"));
        Assert.assertEquals(createdDeleteUser, userRepository.findByUsername(createdDeleteUser.getUsername()));

        userService.deleteUser(createdDeleteUser.getId(), createdDeleteUser.getPassword());

        Assert.assertNull(userRepository.findByUsername("testDeleteUsername"));
        Assert.assertNull(userRepository.findById(testDeleteId));
        Assert.assertNull(userRepository.findByToken(testDeleteToken));
    }
}
