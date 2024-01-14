package com.openclassrooms.starterjwt.controllersTest.unitTest.controller;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("UserController_ClassTest")
@DisplayName("Unit Tests for UserController")
public class UserUnitTest {

    @Mock
    UserMapper userMapper;
    @Mock
    UserService userService;

    UserController userController;

    @BeforeEach
    public void setup() {
        userController = new UserController(userService, userMapper);
    }

    @Test
    @DisplayName("Find user by ID should return response OK with user details")
    public void findById_WithValidId_ShouldReturnResponseOkWithUser() {
        String id = "1";
        User mockUser = mock(User.class);

        when(userService.findById(Long.valueOf(id))).thenReturn(mockUser);

        ResponseEntity<?> response = userController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userMapper.toDto(mockUser));
        verify(userService).findById(Long.valueOf(id));
        verify(userMapper, times(2)).toDto(mockUser);
    }

    @Test
    @DisplayName("Find user by ID should return NOT FOUND for non-existent user")
    public void findById_WithNonExistentId_ShouldReturnNotFound() {
        String id = "1";

        when(userService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = userController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService).findById(Long.valueOf(id));
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Find user by ID with invalid format should return BAD REQUEST")
    public void findById_WithInvalidIdFormat_ShouldReturnBadRequest() {
        String id = "a";

        ResponseEntity<?> response = userController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService, userMapper);
    }

    @Test
    @DisplayName("Delete user by ID with valid ID and User returns OK")
    public void save_ValidIdAndUser_ReturnsOk() {
        String id = "1";
        User mockUser = mock(User.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        Authentication mockAuthentication = mock(Authentication.class);

        when(userService.findById(Long.valueOf(id))).thenReturn(mockUser);
        when(mockUser.getEmail()).thenReturn("openclassroom@gmail.com");
        when(mockUserDetails.getUsername()).thenReturn("openclassroom@gmail.com");
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        ResponseEntity<?> response = userController.save(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).delete(Long.parseLong(id));
    }

    @Test
    @DisplayName("Delete user by ID with invalid ID format returns Bad Request")
    public void save_InvalidIdFormat_ReturnsBadRequest() {
        String id = "a";

        ResponseEntity<?> response = userController.save(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Delete user by ID with non-existent ID returns Not Found")
    public void save_NonExistentId_ReturnsNotFound() {
        String id = "1";

        when(userService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = userController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete user by ID with different user email and username returns Unauthorized")
    public void save_DifferentUserEmailAndUsername_ReturnsUnauthorized() {
        String id = "1";
        User mockUser = mock(User.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        Authentication mockAuthentication = mock(Authentication.class);

        when(userService.findById(Long.valueOf(id))).thenReturn(mockUser);
        when(mockUserDetails.getUsername()).thenReturn("username1");
        when(mockUser.getEmail()).thenReturn("username2");
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);


        ResponseEntity<?> response = userController.save(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
