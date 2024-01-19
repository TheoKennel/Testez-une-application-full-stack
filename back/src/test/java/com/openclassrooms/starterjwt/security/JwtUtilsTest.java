package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String token = "Token";
    private final String jwtSecret = "secretJwt";

    @BeforeEach
    public void setup() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
    }

    @Test
    public void validateJwtToken_ValidToken_ShouldReturnTrue() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);

            Jws mockJws = mock(Jws.class);

            jwtsMockedStatic.when(Jwts::parser).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenReturn(mockJws); // Mock the expected behavior

            assertTrue(jwtUtils.validateJwtToken(token));
        }
    }

    @Test
    public void validateJwtToken_InvalidSignature_ShouldReturnFalse() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);
            when(Jwts.parser()).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenThrow(SignatureException.class);

            assertFalse(jwtUtils.validateJwtToken(token));
        }
    }

    @Test
    public void validateJwtToken_MalformedJwtException_ShouldReturnFalse() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);
            when(Jwts.parser()).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenThrow(MalformedJwtException.class);

            assertFalse(jwtUtils.validateJwtToken(token));
        }
    }

    @Test
    public void validateJwtToken_ExpiredJwtException_ShouldReturnFalse() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);
            when(Jwts.parser()).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenThrow(ExpiredJwtException.class);

            assertFalse(jwtUtils.validateJwtToken(token));
        }
    }

    @Test
    public void validateJwtToken_UnsupportedJwtException_ShouldReturnFalse() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);
            when(Jwts.parser()).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenThrow(UnsupportedJwtException.class);

            assertFalse(jwtUtils.validateJwtToken(token));
        }
    }

    @Test
    public void validateJwtToken_IllegalArgumentException_ShouldReturnFalse() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);
            when(Jwts.parser()).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenThrow(IllegalArgumentException.class);

            assertFalse(jwtUtils.validateJwtToken(token));
        }
    }

    @Test
    public void getUserNameFromJwtToken_ValidToken_ShouldReturnUsername() {
        String expectedUsername = "testUser";
        Claims claims = Jwts.claims().setSubject(expectedUsername);

        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            JwtParser mockJwtParser = mock(JwtParser.class);
            Jws<Claims> mockJws = mock(Jws.class);

            when(Jwts.parser()).thenReturn(mockJwtParser);
            when(mockJwtParser.setSigningKey(jwtSecret)).thenReturn(mockJwtParser);
            when(mockJwtParser.parseClaimsJws(token)).thenReturn(mockJws);
            when(mockJws.getBody()).thenReturn(claims);

            String username = jwtUtils.getUserNameFromJwtToken(token);

            assertThat(expectedUsername).isEqualTo(username);
        }
    }
}
