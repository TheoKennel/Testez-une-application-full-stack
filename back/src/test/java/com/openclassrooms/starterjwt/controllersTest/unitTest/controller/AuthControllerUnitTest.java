package com.openclassrooms.starterjwt.controllersTest.unitTest.controller;


import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("AuthController_Unit_Test")
public class AuthControllerUnitTest {

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @InjectMocks
     AuthController authController;

    @Test
    @DisplayName("Authenticate user with valid credentials should succeed")
    public void authenticateUser_withValidCredentials_ShouldSucceed() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("openclassrooms@gmail.com");
        loginRequest.setPassword("123456");
        Authentication mockAuth = mock(Authentication.class);
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        User mockUser = mock(User.class);

        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtUtils.generateJwtToken(any())).thenReturn("mockToken");
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(mockUser.isAdmin()).thenReturn(false);
        when(mockUserDetails.getUsername()).thenReturn("openclassrooms@gmail.com");
        when(mockUserDetails.getId()).thenReturn(1L);

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertThat(jwtResponse.getToken()).isNotNull();
        assertThat(jwtResponse.getUsername()).isEqualTo(loginRequest.getEmail());
        assertThat(jwtResponse.getId()).isEqualTo(mockUserDetails.getId());
        assertThat(jwtResponse.getLastName()).isEqualTo(mockUserDetails.getLastName());
        assertThat(jwtResponse.getFirstName()).isEqualTo(mockUserDetails.getFirstName());

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail(anyString());
        verify(jwtUtils).generateJwtToken(any());
    }

    @Test
    @DisplayName("Authenticate user with invalid credentials should throw BadCredentialsException")
    public void authenticateUser_withInvalidCredentials_ShouldThrowException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrongemail@gmail.com");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        Exception exception = assertThrows(BadCredentialsException.class, () ->  {
            authController.authenticateUser(loginRequest);
        });
        assertThat(exception).isInstanceOf(BadCredentialsException.class);
        assertThat(exception.getMessage()).isEqualTo("Invalid credentials");
        verifyNoInteractions(jwtUtils, userRepository);
    }

    @Test
    @DisplayName("Register user with valid credentials should succeed")
    public void registerUser_withValidCredentials_ShouldSucceed() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("openclassroom@gmail.com");
        signupRequest.setPassword("123456");
        signupRequest.setFirstName("open");
        signupRequest.setLastName("classroom");
        User mockUser = mock(User.class);

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("passwordEncode");
        when(userRepository.save(any())).thenReturn(mockUser);

        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Register user with existing email should fail")
    public void registerUser_withExistingEmail_ShouldFail() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("openclassroom@gmail.com");
        signupRequest.setPassword("123456");
        signupRequest.setFirstName("open");
        signupRequest.setLastName("classroom");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");
    }
}
