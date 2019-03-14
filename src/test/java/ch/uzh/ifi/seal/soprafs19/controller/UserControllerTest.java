package ch.uzh.ifi.seal.soprafs19.controller;

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
    private UserController userController;

    @Test
    public void validateToken() {
        Assert.assertNull(userRepository.findByUsername("testControllerValidateTokenUsername"));

        User testControllerValidateTokenUser = new User();
        testControllerValidateTokenUser.setUsername("testControllerValidateTokenUsername");
        testControllerValidateTokenUser.setPassword("testControllerValidateTokenPassword");
        testControllerValidateTokenUser.setBirthdate(today);
        testControllerValidateTokenUser.setCreationDate(today);

        userController.createUser(testControllerValidateTokenUser);
        String testControllerValidateTokenUserUsername = testControllerValidateTokenUser.getUsername();
        User createdControllerValidateTokenUser = userRepository.findByUsername(testControllerValidateTokenUserUsername);
        String createdControllerValidateTokenUserToken = createdControllerValidateTokenUser.getToken();
        long createdControllerValidateTokenUserId = createdControllerValidateTokenUser.getId();

        Assert.assertTrue(userController.validateToken(createdControllerValidateTokenUserToken, createdControllerValidateTokenUserId));
        Assert.assertTrue(userController.validateToken(createdControllerValidateTokenUser.getToken(), createdControllerValidateTokenUser.getId()));

        userController.deleteUser(createdControllerValidateTokenUser.getId()); //cleanup
    }

    @Test(expected = InexistingUser.class)
    public void validateTokenInexisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerValidateTokenUsernameInexisting"));

        User testControllerValidateTokenUserInexisting = new User();
        testControllerValidateTokenUserInexisting.setUsername("testControllerValidateTokenUsernameInexisting");
        testControllerValidateTokenUserInexisting.setPassword("testControllerValidateTokenPasswordInexisting");
        testControllerValidateTokenUserInexisting.setBirthdate(today);
        testControllerValidateTokenUserInexisting.setCreationDate(today);

        userController.createUser(testControllerValidateTokenUserInexisting);
        String testControllerValidateTokenUserUsernameInexisting = testControllerValidateTokenUserInexisting.getUsername();
        User createdControllerValidateTokenUserInexisting = userRepository.findByUsername(testControllerValidateTokenUserUsernameInexisting);
        String createdControllerValidateTokenUserTokenInexisting = createdControllerValidateTokenUserInexisting.getToken();
        createdControllerValidateTokenUserTokenInexisting = createdControllerValidateTokenUserTokenInexisting+"_";
        long createdControllerValidateTokenUserIdInexisting = createdControllerValidateTokenUserInexisting.getId();

        Assert.assertTrue(userController.validateToken(createdControllerValidateTokenUserTokenInexisting,
                createdControllerValidateTokenUserIdInexisting));//throws InexistingUser()

        String createdControllerValidateTokenEmptyToken = "";
        Assert.assertTrue(userController.validateToken(createdControllerValidateTokenEmptyToken,
                createdControllerValidateTokenUserIdInexisting));//throws InexistingUser()

        userController.deleteUser(createdControllerValidateTokenUserInexisting.getId()); //cleanup
    }

    @Test(expected = InvalidToken.class)
    public void validateTokenInvalid() {
        Assert.assertNull(userRepository.findByUsername("testControllerValidateTokenUsernameInvalid"));

        User testControllerValidateTokenUserInvalid = new User();
        testControllerValidateTokenUserInvalid.setUsername("testControllerValidateTokenUsernameInvalid");
        testControllerValidateTokenUserInvalid.setPassword("testControllerValidateTokenPasswordInvalid");
        testControllerValidateTokenUserInvalid.setBirthdate(today);
        testControllerValidateTokenUserInvalid.setCreationDate(today);

        userController.createUser(testControllerValidateTokenUserInvalid);
        String testControllerValidateTokenUserUsername = testControllerValidateTokenUserInvalid.getUsername();
        User createdControllerValidateTokenUserInvalid = userRepository.findByUsername(testControllerValidateTokenUserUsername);
        long createdControllerValidateTokenUserIdInvalid = createdControllerValidateTokenUserInvalid.getId();

        String createdControllerValidateTokenNullToken = null;
        Assert.assertTrue(userController.validateToken(createdControllerValidateTokenNullToken, createdControllerValidateTokenUserIdInvalid));//throws InvalidToken()

        User testControllerValidateTokenUserDifferent = new User();
        testControllerValidateTokenUserDifferent.setUsername("testControllerValidateTokenUsernameDifferent");
        testControllerValidateTokenUserDifferent.setPassword("testControllerValidateTokenPasswordDifferent");
        testControllerValidateTokenUserDifferent.setBirthdate(today);
        testControllerValidateTokenUserDifferent.setCreationDate(today);

        userController.createUser(testControllerValidateTokenUserDifferent);
        String testControllerValidateTokenUserUsernameDifferent = testControllerValidateTokenUserInvalid.getUsername();
        User createdControllerValidateTokenUserDifferent = userRepository.findByUsername(testControllerValidateTokenUserUsernameDifferent);

        String createdControllerValidateTokenDifferentToken = createdControllerValidateTokenUserDifferent.getToken();
        Assert.assertTrue(userController.validateToken(createdControllerValidateTokenDifferentToken, createdControllerValidateTokenUserIdInvalid));//throws InvalidToken()

        userController.deleteUser(createdControllerValidateTokenUserInvalid.getId()); //cleanup
    }

    //TODO: include error cases
    //TODO: check for response status (error state via Exception class, maybe check for success via new success exception?)
    @Test
    public void validatePassword() {
        Assert.assertNull(userRepository.findByUsername("testControllerValidatePasswordUsername"));

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

        userController.deleteUser(createdControllerValidatePasswordUser.getId()); //cleanup
    }

    @Test(expected = InvalidPassword.class)
    public void validatePasswordInvalid() {
        Assert.assertNull(userRepository.findByUsername("testControllerValidatePasswordUsernameInvalid"));

        User testControllerValidatePasswordUserInvalid = new User();
        testControllerValidatePasswordUserInvalid.setUsername("testControllerValidatePasswordUsernameInvalid");
        testControllerValidatePasswordUserInvalid.setPassword("testControllerValidatePasswordPasswordInvalid");
        testControllerValidatePasswordUserInvalid.setCreationDate(today);
        testControllerValidatePasswordUserInvalid.setBirthdate(today);

        userController.createUser(testControllerValidatePasswordUserInvalid);
        String testControllerValidatePasswordUserUsernameInvalid = testControllerValidatePasswordUserInvalid.getUsername();
        User createdControllerValidatePasswordUserInvalid = userRepository.findByUsername(testControllerValidatePasswordUserUsernameInvalid);
        String createdControllerValidatePasswordUserPasswordInvalid = createdControllerValidatePasswordUserInvalid.getPassword();
        createdControllerValidatePasswordUserPasswordInvalid = createdControllerValidatePasswordUserPasswordInvalid+"_";
        long createdControllerValidatePasswordUserIdInvalid = createdControllerValidatePasswordUserInvalid.getId();

        Assert.assertTrue(userController.validatePassword(createdControllerValidatePasswordUserPasswordInvalid,
                createdControllerValidatePasswordUserIdInvalid));//throws InvalidPassword()

        String createdControllerValidatePasswordNullPassword = null;
        Assert.assertTrue(userController.validatePassword(createdControllerValidatePasswordNullPassword,
                createdControllerValidatePasswordUserIdInvalid));//throws InvalidPassword()

        String createdControllerValidatePasswordEmptyPassword = "";
        Assert.assertTrue(userController.validatePassword(createdControllerValidatePasswordEmptyPassword,
                createdControllerValidatePasswordUserIdInvalid));//throws InvalidPassword()

        userController.deleteUser(createdControllerValidatePasswordUserInvalid.getId()); //cleanup
    }

    @Test(expected = InexistingUser.class)
    public void validatePasswordInexisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerValidatePasswordUsernameInexisting"));

        User testControllerValidatePasswordUserInexisting = new User();
        testControllerValidatePasswordUserInexisting.setUsername("testControllerValidatePasswordUsernameInexisting");
        testControllerValidatePasswordUserInexisting.setPassword("testControllerValidatePasswordPasswordInexisting");
        testControllerValidatePasswordUserInexisting.setCreationDate(today);
        testControllerValidatePasswordUserInexisting.setBirthdate(today);

        userController.createUser(testControllerValidatePasswordUserInexisting);
        String testControllerValidatePasswordUserUsernameInexisting = testControllerValidatePasswordUserInexisting.getUsername();
        User createdControllerValidatePasswordUserInexisting = userRepository.findByUsername(testControllerValidatePasswordUserUsernameInexisting);
        String createdControllerValidatePasswordUserPasswordInexisting = createdControllerValidatePasswordUserInexisting.getPassword();
        createdControllerValidatePasswordUserPasswordInexisting = createdControllerValidatePasswordUserPasswordInexisting+"_";
        long createdControllerValidatePasswordUserIdInexisting = createdControllerValidatePasswordUserInexisting.getId();

        Assert.assertTrue(userController.validatePassword(createdControllerValidatePasswordUserPasswordInexisting,
                (-1)*createdControllerValidatePasswordUserIdInexisting));//throws InexistingUser()

        userController.deleteUser(createdControllerValidatePasswordUserInexisting.getId()); //cleanup
    }

    @Test
    public void all() {
    }

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testControllerCreateUsername"));

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

        Assert.assertEquals(testControllerCreateUser.getUsername(), createdControllerCreateUser.getUsername());
        Assert.assertEquals(testControllerCreateUser.getFirstName(), createdControllerCreateUser.getFirstName());
        Assert.assertEquals(testControllerCreateUser.getLastName(), createdControllerCreateUser.getLastName());
        Assert.assertEquals(testControllerCreateUser.getPassword(), createdControllerCreateUser.getPassword());
        Assert.assertEquals(testControllerCreateUser.getBirthdate(), createdControllerCreateUser.getBirthdate());
        Assert.assertEquals(testControllerCreateUser.getCreationDate(), createdControllerCreateUser.getCreationDate());
        Assert.assertNotNull(createdControllerCreateUser.getId());
        Assert.assertNotNull(createdControllerCreateUser.getToken());

        userController.deleteUser(createdControllerCreateUser.getId()); //cleanup
    }

    @Test(expected = ExistingUser.class)
    public void createUserExisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerCreateUsernameExisting"));

        User testControllerCreateUserExisting = new User();
        testControllerCreateUserExisting.setFirstName("testControllerCreateFirstNameExisting");
        testControllerCreateUserExisting.setLastName("testControllerCreateLastNameExisting");
        testControllerCreateUserExisting.setUsername("testControllerCreateUsernameExisting");
        testControllerCreateUserExisting.setPassword("testControllerCreatePasswordExisting");
        testControllerCreateUserExisting.setBirthdate(today);
        testControllerCreateUserExisting.setCreationDate(today);

        String createdControllerCreateUserUrlExisting = userController.createUser(testControllerCreateUserExisting);
        Assert.assertTrue(createdControllerCreateUserUrlExisting.startsWith("/users/"));

        String createdControllerCreateUserIdStringExisting = createdControllerCreateUserUrlExisting.substring(7);
        long createdControllerCreateUserIdExisting = Long.parseLong(createdControllerCreateUserIdStringExisting);
        User createdControllerCreateUserExisting = userRepository.findById(createdControllerCreateUserIdExisting);

        User testControllerCreateUserDefinitelyExisting = new User();
        testControllerCreateUserDefinitelyExisting.setUsername("testControllerCreateUsernameExisting");//same Username to find user
        testControllerCreateUserDefinitelyExisting.setPassword("testControllerCreatePasswordDefinitelyExisting");
        testControllerCreateUserDefinitelyExisting.setBirthdate(today);
        testControllerCreateUserDefinitelyExisting.setCreationDate(today);

        String createdControllerCreateUserExistingUrl = userController.createUser(testControllerCreateUserDefinitelyExisting);//throws ExistingUser()
        Assert.assertNull(createdControllerCreateUserExistingUrl);

        userController.deleteUser(createdControllerCreateUserExisting.getId()); //cleanup
    }

    @Test
    public void getUserId() {
        Assert.assertNull(userRepository.findByUsername("testControllerGetIdUsername"));

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

        userController.deleteUser(createdControllerGetIdUser.getId()); //cleanup
    }

    @Test(expected = InexistingUser.class)
    public void getUserIdInexisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerGetIdUsernameInexisting"));

        User testControllerGetIdUserInexisting = new User();
        testControllerGetIdUserInexisting.setUsername("testControllerGetIdUsernameInexisting");
        testControllerGetIdUserInexisting.setPassword("testControllerGetIdPasswordInexisting");
        testControllerGetIdUserInexisting.setBirthdate(today);
        testControllerGetIdUserInexisting.setCreationDate(today);

        userController.createUser(testControllerGetIdUserInexisting);
        String testControllerGetIdUserUsernameInexisting = testControllerGetIdUserInexisting.getUsername();
        User createdControllerGetIdUserInexisting = userRepository.findByUsername(testControllerGetIdUserUsernameInexisting);

        User createdControllerGetIdUserDefinitelyInexisting = userController.getUserId((-1)*createdControllerGetIdUserInexisting.getId());//throws InexistingUser()
        Assert.assertNull(createdControllerGetIdUserDefinitelyInexisting);

        userController.deleteUser(createdControllerGetIdUserInexisting.getId()); //cleanup
    }

    @Test
    public void getUserUsername() {
        Assert.assertNull(userRepository.findByUsername("testControllerGetUsernameUsername"));

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

        userController.deleteUser(createdControllerGetUsernameUser.getId()); //cleanup
    }

    @Test(expected = InexistingUser.class)
    public void getUserUsernameInexisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerGetUsernameUsernameInexisting"));

        User testControllerGetUsernameUserInexisting = new User();
        testControllerGetUsernameUserInexisting.setUsername("testControllerGetUsernameUsernameInexisting");
        testControllerGetUsernameUserInexisting.setPassword("testControllerGetUsernamePasswordInexisting");
        testControllerGetUsernameUserInexisting.setBirthdate(today);
        testControllerGetUsernameUserInexisting.setCreationDate(today);

        userController.createUser(testControllerGetUsernameUserInexisting);
        String testControllerGetUsernameUserUsernameInexisting = testControllerGetUsernameUserInexisting.getUsername();
        User createdControllerGetUsernameUserInexisting = userRepository.findByUsername(testControllerGetUsernameUserUsernameInexisting);

        User createdControllerGetUsernameUserDefinitelyInexisting = userController.getUserUsername(createdControllerGetUsernameUserInexisting.getUsername()+"&_");
        Assert.assertNull(createdControllerGetUsernameUserDefinitelyInexisting);

        userController.deleteUser(createdControllerGetUsernameUserInexisting.getId()); //cleanup
    }

    //TODO: add error update test
    @Test
    public void updateUser() {
        Assert.assertNull(userRepository.findByUsername("testControllerUpdateUsername"));
        Assert.assertNull(userRepository.findByUsername("testControllerUpdatedUname"));

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
        createdControllerUpdateUserOriginal = userRepository.findByUsername(testControllerUpdateUserUpdated.getUsername());

        System.out.println(createdControllerUpdateUserOriginalId);
        System.out.println((long)createdControllerUpdateUserOriginal.getId());
        Assert.assertEquals(createdControllerUpdateUserOriginalId, (long)createdControllerUpdateUserOriginal.getId()); //casting for unambiguous function call
        Assert.assertEquals(testControllerUpdateUserUpdated.getId(), createdControllerUpdateUserOriginal.getId());
        Assert.assertEquals(testControllerUpdateUserUpdated.getFirstName(), createdControllerUpdateUserOriginal.getFirstName());
        Assert.assertEquals(testControllerUpdateUserUpdated.getLastName(), createdControllerUpdateUserOriginal.getLastName());
        Assert.assertEquals(testControllerUpdateUserUpdated.getUsername(), createdControllerUpdateUserOriginal.getUsername());
        Assert.assertEquals(testControllerUpdateUserUpdated.getPassword(), createdControllerUpdateUserOriginal.getPassword());

        userController.deleteUser(createdControllerUpdateUserOriginal.getId()); //cleanup
    }

    @Test
    public void loginUser() {
        Assert.assertNull(userRepository.findByUsername("testControllerLoginUsername"));

        User testControllerLoginUser = new User();
        testControllerLoginUser.setUsername("testControllerLoginUsername");
        testControllerLoginUser.setPassword("testControllerLoginPassword");
        testControllerLoginUser.setBirthdate(today);
        testControllerLoginUser.setCreationDate(today);
        String testControllerLoginUsername = testControllerLoginUser.getUsername();

        userController.createUser(testControllerLoginUser);
        User createdControllerLoginUser = userRepository.findByUsername(testControllerLoginUsername);
        Assert.assertEquals(UserStatus.OFFLINE, createdControllerLoginUser.getStatus());

        userController.loginUser(createdControllerLoginUser.getUsername());
        //userController.loginUser(createdControllerLoginUser.getUsername(), createdControllerLoginUser.getPassword());
        createdControllerLoginUser = userRepository.findByUsername(createdControllerLoginUser.getUsername());//get updated user
        Assert.assertEquals(UserStatus.ONLINE, createdControllerLoginUser.getStatus());

        userController.deleteUser(createdControllerLoginUser.getId()); //cleanup
    }

    @Test(expected = InexistingUser.class)
    public void loginUserInexisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerLoginUsernameInexisting"));

        User testControllerLoginUserInexisting = new User();
        testControllerLoginUserInexisting.setUsername("testControllerLoginUsernameInexisting");
        testControllerLoginUserInexisting.setPassword("testControllerLoginPasswordInexisting");
        testControllerLoginUserInexisting.setBirthdate(today);
        testControllerLoginUserInexisting.setCreationDate(today);
        String testControllerLoginUsernameInexisting = testControllerLoginUserInexisting.getUsername();

        userController.createUser(testControllerLoginUserInexisting);
        User createdControllerLoginUserInexisting = userRepository.findByUsername(testControllerLoginUsernameInexisting);
        Assert.assertEquals(createdControllerLoginUserInexisting.getStatus(), UserStatus.OFFLINE);

        userController.loginUser(createdControllerLoginUserInexisting.getUsername()+"&_");//throws InexistingUser()
        //userController.loginUser(createdControllerLoginUserInexisting.getUsername()+"&_", createdControllerLoginUserInexisting.getPassword());//throws InexistingUser()
        Assert.assertNotEquals(createdControllerLoginUserInexisting.getStatus(), UserStatus.ONLINE);
        Assert.assertEquals(createdControllerLoginUserInexisting.getStatus(), UserStatus.OFFLINE);

        userController.deleteUser(createdControllerLoginUserInexisting.getId()); //cleanup
    }

    /*
    @Test(expected = InvalidPassword.class)
    public void loginUserInvalid() {
        Assert.assertNull(userRepository.findByUsername("testControllerLoginUsernameInvalid"));

        User testControllerLoginUserInvalid = new User();
        testControllerLoginUserInvalid.setUsername("testControllerLoginUsernameInvalid");
        testControllerLoginUserInvalid.setPassword("testControllerLoginPasswordInvalid");
        testControllerLoginUserInvalid.setBirthdate(today);
        testControllerLoginUserInvalid.setCreationDate(today);
        String testControllerLoginUsernameInvalid = testControllerLoginUserInvalid.getUsername();

        userController.createUser(testControllerLoginUserInvalid);
        User createdControllerLoginUserInvalid = userRepository.findByUsername(testControllerLoginUsernameInvalid);
        Assert.assertEquals(createdControllerLoginUserInvalid.getStatus(), UserStatus.OFFLINE);

        userController.loginUser(createdControllerLoginUserInvalid.getUsername());//throws InvalidPassword()
        //userController.loginUser(createdControllerLoginUserInvalid.getUsername(), createdControllerLoginUserInvalid.getPassword()+"_");//throws InvalidPassword()
        Assert.assertNotEquals(createdControllerLoginUserInvalid.getStatus(), UserStatus.ONLINE);
        Assert.assertEquals(createdControllerLoginUserInvalid.getStatus(), UserStatus.OFFLINE);

        userController.deleteUser(createdControllerLoginUserInvalid.getId()); //cleanup
    }
    */

    @Test
    public void logoutUser() {
        Assert.assertNull(userRepository.findByUsername("testControllerLogoutUsername"));

        User testControllerLogoutUser = new User();
        testControllerLogoutUser.setUsername("testControllerLogoutUsername");
        testControllerLogoutUser.setPassword("testControllerLogoutPassword");
        testControllerLogoutUser.setBirthdate(today);
        testControllerLogoutUser.setCreationDate(today);
        String testControllerLogoutUsername = testControllerLogoutUser.getUsername();

        userController.createUser(testControllerLogoutUser);
        User createdControllerLogoutUser = userRepository.findByUsername(testControllerLogoutUsername);
        Assert.assertEquals(UserStatus.OFFLINE, createdControllerLogoutUser.getStatus());

        userController.loginUser(createdControllerLogoutUser.getUsername());
        createdControllerLogoutUser = userRepository.findByUsername(createdControllerLogoutUser.getUsername());
        Assert.assertEquals(UserStatus.ONLINE, createdControllerLogoutUser.getStatus());

        userController.logoutUser(createdControllerLogoutUser.getUsername());
        createdControllerLogoutUser = userRepository.findByUsername(createdControllerLogoutUser.getUsername());
        Assert.assertEquals(UserStatus.OFFLINE, createdControllerLogoutUser.getStatus());

        userController.deleteUser(createdControllerLogoutUser.getId()); //cleanup
    }

    @Test(expected = InexistingUser.class)
    public void logoutUserInexisting() {
        Assert.assertNull(userRepository.findByUsername("testControllerLogoutUsernameInexisting"));

        User testControllerLogoutUserInexisting = new User();
        testControllerLogoutUserInexisting.setUsername("testControllerLogoutUsernameInexisting");
        testControllerLogoutUserInexisting.setPassword("testControllerLogoutPasswordInexisting");
        testControllerLogoutUserInexisting.setBirthdate(today);
        testControllerLogoutUserInexisting.setCreationDate(today);
        String testControllerLogoutUsernameInexisting = testControllerLogoutUserInexisting.getUsername();

        userController.createUser(testControllerLogoutUserInexisting);
        User createdControllerLogoutUserInexisting = userRepository.findByUsername(testControllerLogoutUsernameInexisting);
        Assert.assertEquals(UserStatus.OFFLINE, createdControllerLogoutUserInexisting.getStatus());

        userController.loginUser(createdControllerLogoutUserInexisting.getUsername());
        createdControllerLogoutUserInexisting = userRepository.findByUsername(createdControllerLogoutUserInexisting.getUsername());
        Assert.assertEquals(UserStatus.ONLINE, createdControllerLogoutUserInexisting.getStatus());

        userController.logoutUser(createdControllerLogoutUserInexisting.getUsername()+"&_");//throws InexistingUser()
        createdControllerLogoutUserInexisting = userRepository.findByUsername(createdControllerLogoutUserInexisting.getUsername());
        Assert.assertNotEquals(UserStatus.OFFLINE, createdControllerLogoutUserInexisting.getStatus());
        Assert.assertEquals(UserStatus.ONLINE, createdControllerLogoutUserInexisting.getStatus());

        userController.deleteUser(createdControllerLogoutUserInexisting.getId()); //cleanup
    }

    @Test
    public void deleteUser() {
        Assert.assertNull(userRepository.findByUsername("testControllerDeleteUsername"));

        User testControllerDeleteUser = new User();
        testControllerDeleteUser.setUsername("testControllerDeleteUsername");
        testControllerDeleteUser.setPassword("testControllerDeletePassword");
        testControllerDeleteUser.setBirthdate(today);
        testControllerDeleteUser.setCreationDate(today);
        String testControllerDeleteUsername = testControllerDeleteUser.getUsername();

        userController.createUser(testControllerDeleteUser);
        User createdControllerDeleteUser = userRepository.findByUsername(testControllerDeleteUsername);
        Assert.assertNotNull(createdControllerDeleteUser);
        Assert.assertEquals(testControllerDeleteUsername, createdControllerDeleteUser.getUsername());

        userController.deleteUser(createdControllerDeleteUser.getId()); //represents cleanup
        //userController.deleteUser(createdControllerDeleteUser.getId(), createdControllerDeleteUser.getPassword()); //represents cleanup
        Assert.assertNull(userRepository.findByUsername(testControllerDeleteUsername));
    }

    /*
    @Test(expected = InvalidPassword.class)
    public void deleteUserInvalid() {
        Assert.assertNull(userRepository.findByUsername("testControllerDeleteUsernameInvalid"));

        User testControllerDeleteUserInvalid = new User();
        testControllerDeleteUserInvalid.setUsername("testControllerDeleteUsernameInvalid");
        testControllerDeleteUserInvalid.setPassword("testControllerDeletePasswordInvalid");
        testControllerDeleteUserInvalid.setBirthdate(today);
        testControllerDeleteUserInvalid.setCreationDate(today);
        String testControllerDeleteUsernameInvalid = testControllerDeleteUserInvalid.getUsername();

        userController.createUser(testControllerDeleteUserInvalid);
        User createdControllerDeleteUserInvalid = userRepository.findByUsername(testControllerDeleteUsernameInvalid);
        Assert.assertNotNull(createdControllerDeleteUserInvalid);
        Assert.assertEquals(testControllerDeleteUsernameInvalid, createdControllerDeleteUserInvalid.getUsername());

        userController.deleteUser(createdControllerDeleteUserInvalid.getId()); //represents cleanup
        //userController.deleteUser(createdControllerDeleteUserInvalid.getId(), createdControllerDeleteUserInvalid.getPassword()+"_");//throws InvalidPassword()
        // represents cleanup
        Assert.assertNotNull(userRepository.findByUsername(testControllerDeleteUsernameInvalid));
        Assert.assertEquals(createdControllerDeleteUserInvalid, userRepository.findByUsername(testControllerDeleteUsernameInvalid));
    }
    */
}
