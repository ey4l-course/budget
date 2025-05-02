package com.budget.Users;

import com.budget.Users.model.User;
import com.budget.Users.model.UserLogin;
import com.budget.Users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String testMail = "testEmail@yahoo.com";
    private final String testHashedEmail = hashGenerator(testMail);
    private final String testPassword = "!QAZ2wsx";
    private final User registerUser = new User(testMail, testPassword);
    private final UserLogin login = new UserLogin(testHashedEmail, testPassword);

    private String hashGenerator (String email) {
        try {
            byte[] hashedBytes = MessageDigest.getInstance("SHA-256").digest(email.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testLoginWithCorrectCredentials(CapturedOutput output) {
        System.out.println(restTemplate.postForEntity("/api/auth", registerUser, String.class));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", login, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ok", response.getBody());
        assertThat(output).contains("successfully logged in");
    }

    @Test
    void testLoginWithWrongPassword(CapturedOutput output) {
        login.setPassword("Wrong");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", login, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        assertThat(output).contains("Fail at login.");
    }

    @Test
    void testLoginWithNonExistentUser(CapturedOutput output) {
        UserLogin login = new UserLogin("ghost@mail.com", "any");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", login, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        assertThat(output).contains("Fail at login.").contains("Attempted login with non existing user");
    }


    //TO RUN THIS TEST "UNIQUE" CONSTRAINS MUST BE REMOVED IN DB
    @Test
    void testLoginWithDuplicateEmail(CapturedOutput output) {
        System.out.println(restTemplate.postForEntity("/api/auth", registerUser, String.class));
        System.out.println(restTemplate.postForEntity("/api/auth", registerUser, String.class));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", login, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Error ref:"));
        assertThat(output).contains("Duplicate E-mail found in DB");
    }
}
