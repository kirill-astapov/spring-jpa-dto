package spring.dto.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@SpringJUnitConfig(initializers = UserRepositoryTest.Initializer.class)
public class UserRepositoryTest {

    @ClassRule
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>()
        .withUsername("test")
        .withPassword("test")
        .withDatabaseName("test");

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @Before
    public void init(){
        testUser = new User();
        testUser.setName("name");
        testUser.setPhone("phone");
        testUser.setPassword("password");
        userRepository.save(testUser);
    }

    @After
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void testRepository() {
        var user = userRepository.findAll().get(0);
        log.info("testRepository: user={}", user);
        assertNotNull(user);
        checkUser(user.getName(), user.getPhone());

        var userDto = userRepository.findAllDto().get(0);
        log.info("testRepository: userDto={}", userDto);
        assertNotNull(userDto);
        checkUser(userDto.getName(), userDto.getPhone());

        var userSnapshot = userRepository.findAllSnapshot().get(0);
        log.info("testRepository: userSnapshot={}", userSnapshot);
        assertNotNull(userSnapshot);
        checkUser(userSnapshot.getName(), userSnapshot.getPhone());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues appProperties = TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()
            );

            appProperties.applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    private void checkUser(String name, String phone) {
        assertEquals("The wrong name", testUser.getName(), name);
        assertEquals("The wrong phone", testUser.getPhone(), phone);
    }
}