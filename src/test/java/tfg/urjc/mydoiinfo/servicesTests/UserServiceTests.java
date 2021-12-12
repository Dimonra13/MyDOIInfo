package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import tfg.urjc.mydoiinfo.domain.entities.User;
import tfg.urjc.mydoiinfo.domain.repositories.UserRepository;
import tfg.urjc.mydoiinfo.services.UserService;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeAll
    void setUp(){
        userRepository.save(new User("Test1234","Test1234",new HashSet<>(Arrays.asList("ADMIN","USER"))));
    }

    @Test
    public void RegisterNewUserNullUsernameTest(){
        //GIVEN: A null username
        String username = null;
        //AND: A valid password
        String password = "test1234";
        //AND: A valid type
        String type = "ADMIN";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserShortUsernameTest(){
        //GIVEN: A short username
        String username = "short";
        //AND: A valid password
        String password = "test1234";
        //AND: A valid type
        String type = "ADMIN";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserNullPasswordTest(){
        //GIVEN: A valid username
        String username = "User1234Test";
        //AND: A null password
        String password = null;
        //AND: A valid type
        String type = "ADMIN";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserShortPasswordTest(){
        //GIVEN: A valid username
        String username = "User1234Test";
        //AND: A short password
        String password = "test";
        //AND: A valid type
        String type = "ADMIN";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserNullTypeTest(){
        //GIVEN: A valid username
        String username = "User1234Test";
        //AND: A valid password
        String password = "test1234";
        //AND: A null type
        String type = null;

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserInvalidTypeTest(){
        //GIVEN: A valid username
        String username = "User1234Test";
        //AND: A valid password
        String password = "test1234";
        //AND: An invalid type
        String type = "WRONG";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserAlreadyRegisterTest(){
        //GIVEN: A valid username (already register)
        String username = "Test1234";
        //AND: A valid password
        String password = "Test1234";
        //AND: A correct type
        String type = "ADMIN";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void RegisterNewUserCorrectUserTest(){
        //GIVEN: A valid username (not register yet)
        String username = "User1234Test";
        //AND: A valid password
        String password = "Test1234";
        //AND: A correct type
        String type = "ADMIN";

        //WHEN: The registerNewUser method is called
        User output = userService.registerNewUser(username,password,type);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output must be the user save in the database with the specified username
        assertEquals(userRepository.findFirstByUsername(username),output);
    }
}
