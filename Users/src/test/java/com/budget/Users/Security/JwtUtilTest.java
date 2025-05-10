package com.budget.Users.Security;

import com.budget.Users.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secretKey = "ThisIsASecretKeyThatIsLongEnough123456!";
    private long expirationTime = 600000; // 10 minutes
    private long refreshExpirationTime = 14400000; // 4 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secretKey, expirationTime, refreshExpirationTime);
    }

    @Test
    void testGenerateTokenAndExtractClaims() {
        // Generate a random UUID
        String uuid = UUID.randomUUID().toString();

        // Generate token
        String token = jwtUtil.generateToken(uuid);

        // Extract claims
        String extractedUuid = jwtUtil.extractUuid(token);
        Claims claims = jwtUtil.extractAllClaims(token);

        // Validate extracted UUID
        assertEquals(uuid, extractedUuid, "Extracted UUID should match the original UUID.");

        // Validate expiration is set
        Date expirationDate = claims.getExpiration();
        assertNotNull(expirationDate, "Expiration date should not be null.");
        assertTrue(expirationDate.after(new Date()), "Expiration should be in the future.");

        // Validate token is valid
        assertTrue(jwtUtil.validateToken(token, uuid), "Token should be valid for the given UUID.");
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        // Generate token with a very short expiration time (1 sec)
        JwtUtil shortLivedJwtUtil = new JwtUtil(secretKey, 1000, 3000);
        String uuid = UUID.randomUUID().toString();
        String token = shortLivedJwtUtil.generateToken(uuid);

        // Wait for token to expire
        Thread.sleep(2000);

        // Ensure token is expired
        assertFalse(shortLivedJwtUtil.validateToken(token, uuid), "Expired token should be invalid.");
    }
}
