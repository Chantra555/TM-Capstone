y ihpackage com.muro.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @Test
    public void generateAndValidateToken() {
        String username = "testuser";
        String token = JwtUtil.generateToken(username);
        assertNotNull(token);
        assertTrue(JwtUtil.validateToken(token));
        assertEquals(username, JwtUtil.extractUsername(token));

        // Test Bearer prefix handling
        String bearer = "Bearer " + token;
        assertTrue(JwtUtil.validateToken(bearer));
        assertEquals(username, JwtUtil.extractUsername(bearer));
    }

    @Test
    public void invalidTokenIsRejected() {
        String bad = "this.is.not.a.jwt";
        assertFalse(JwtUtil.validateToken(bad));
        assertThrows(Exception.class, () -> JwtUtil.extractAllClaims(bad));
    }
}

