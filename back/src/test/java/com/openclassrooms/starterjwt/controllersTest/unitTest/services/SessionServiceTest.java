package com.openclassrooms.starterjwt.controllersTest.unitTest.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;
    @Mock
    UserRepository userRepository;
    SessionService sessionService;

    @BeforeEach
    public void setup() {
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    @DisplayName("Participate with valid ID and user ID should save session")
    public void participate_WithValidIdAndUserId_ShouldSaveSession() {
        Long id = 1L;
        Long userId = 2L;
        User mockUser = mock(User.class);
        Session mockSession = mock(Session.class);
        List<User> users = new ArrayList<>();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockSession.getUsers()).thenReturn(users);

        sessionService.participate(id, userId);

        assertThat(users).contains(mockUser);
        verify(sessionRepository).save(mockSession);
    }

    @Test
    @DisplayName("Participate with session null should return not found exception")
    public void participate_WithSessionNull_ShouldReturnNotFoundException() {
        Long id = 1L;
        Long userId = 1L;
        User mockUser = mock(User.class);
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        assertThatThrownBy(() -> {
            sessionService.participate(id, userId);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Participate with user already participate should return Bad Request Exception")
    public void participate_WithAlreadyParticipate_ShouldReturnBadRequestException() {
        Long id = 1L;
        Long userId = 1L;
        User mockUser = mock(User.class);
        Session mockSession = mock(Session.class);
        List<User> users = new ArrayList<>();
        users.add(mockUser);

        when(mockUser.getId()).thenReturn(userId);
        when(sessionRepository.findById(id)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockSession.getUsers()).thenReturn(users);

        assertThatThrownBy(() -> {
            sessionService.participate(id, userId);
        }).isInstanceOf(BadRequestException.class);
        verify(sessionRepository).findById(id);
        verify(userRepository).findById(userId);
    }


    //Test bug impossible à valider ?
    @Test
    @Disabled
    @DisplayName("No longer participate with valid session and user should remove user from session")
    public void noLongerParticipate_WithValidSessionAndUser_ShouldRemoveUser() {
        Long id = 1L;
        Long userId = 1L;
        Session mockSession = mock(Session.class);
        User mockUser = mock(User.class);
        List<User> users = new ArrayList<>();
        users.add(mockUser);

        when(sessionRepository.findById(id)).thenReturn(Optional.of(mockSession));
        when(mockSession.getUsers()).thenReturn(users);
        when(mockUser.getId()).thenReturn(userId);

        sessionService.noLongerParticipate(id, userId);
        assertThat(users).doesNotContain(mockUser);
        verify(mockSession).setUsers(any());
        verify(sessionRepository).save(mockSession);
    }

    @Test
    @DisplayName("No longer participate with non-existent session should throw NotFoundException")
    public void noLongerParticipate_WithNonExistentSession_ShouldThrowNotFoundException() {
        Long id = 1L;
        Long userId = 1L;
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            sessionService.noLongerParticipate(id, userId);
        })
                .isInstanceOf(NotFoundException.class);

    }

    @Test
    @DisplayName("No longer participate with user not in session should throw BadRequestException")
    public void noLongerParticipate_WithUserNotInSession_ShouldThrowBadRequestException() {
        Long id = 1L;
        Long userId = 1L;
        Session mockSession = mock(Session.class);
        List<User> mockUsers = new ArrayList<>();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(mockSession));
        when(mockSession.getUsers()).thenReturn(mockUsers);

        assertThatThrownBy(() -> {
            sessionService.noLongerParticipate(id, userId);
        }).isInstanceOf(BadRequestException.class);
    }
}
