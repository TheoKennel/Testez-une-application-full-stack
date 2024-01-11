package com.openclassrooms.starterjwt.controllersTest.unitaire;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("UserController_ClassTest")
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
    @Tag("FindById_Id_UserController")
    public void findById_Id_responseOk() {
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
    @Tag("FindById_Id_UserController")
    public void findById_Id_responseNotFound() {
        String id = "1";

        when(userService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = userController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService).findById(Long.valueOf(id));
        verifyNoInteractions(userMapper);
    }

    @Test
    @Tag("FindById_Id_UserController")
    public void findById_Id_responseBadRequest() {
        String id = "a";

        ResponseEntity<?> response = userController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService, userMapper);
    }

//    @Test
//    @Tag("FindById_Id_UserController")
//    public void save_Id_responseOk() {
//        String id = "1";
//        User mockUser = mock(User.class);
//
//        when(userService.findById(Long.valueOf(id))).thenReturn(mockUser);
//
//        ResponseEntity<?> response = userController.findById(id);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(userMapper.toDto(mockUser));
//        verify(userService).findById(Long.valueOf(id));
//        verify(userMapper, times(2)).toDto(mockUser);
//    }
}
