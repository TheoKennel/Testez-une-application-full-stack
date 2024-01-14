package com.openclassrooms.starterjwt.controllersTest.unitTest.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("SessionController_Tests")
@DisplayName("Unit Tests for SessionController")
public class SessionUnitTest {

    @Mock
    SessionMapper sessionMapper;
    @Mock
    SessionService sessionService;
    SessionController sessionController;

    @BeforeEach
    public void setup() {
        sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    @DisplayName("Find session by ID should return response OK")
    public void findById_ShouldFindSession_ResponseOk() {
        String id = "1";
        Session mockSession = mock(Session.class);
        when(sessionService.getById(Long.valueOf(id))).thenReturn(mockSession);

        ResponseEntity<?> response = sessionController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionMapper.toDto(mockSession));

        verify(sessionService).getById(Long.valueOf(id));
        verify(sessionMapper,times(2)).toDto(mockSession);
    }

    @Test
    @DisplayName("Find session by ID should return NOT FOUND for non-existent session")
    public void findById_ShouldNotFindSession_NotFound() {
        String id = "1";

        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verifyNoInteractions(sessionMapper);
    }

    @Test
    @DisplayName("Find session by invalid ID should return BAD REQUEST")
    public void findById_WithInvalidId_ShouldReturnBadRequest() {
        String id = "invalid";

        ResponseEntity<?> response = sessionController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    @DisplayName("Get all sessions should return response OK")
    public void findAll_ShouldReturnResponseOk() {
        Session mockSession = mock(Session.class);
        List<Session> mockSessions = Collections.singletonList(mockSession);
        when(sessionService.findAll()).thenReturn(mockSessions);

        ResponseEntity<?> response = sessionController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionMapper.toDto(mockSessions));
        verify(sessionService).findAll();
    }

    @Test
    @DisplayName("Create session should return response OK")
    public void create_SessionDto_ShouldReturnResponseOk() {
        // EST ce que je dois test les logs ?
        SessionDto sessionDto = new SessionDto(
                12L,
                "test",
                new Date(),
                12L,
                "Description of the session here",
                Arrays.asList(1L, 2L, 3L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Session mockSession = mock(Session.class);
        when(sessionService.create(sessionMapper.toEntity(sessionDto))).thenReturn(mockSession);
        ResponseEntity<?> response = sessionController.create(sessionDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionMapper.toDto(mockSession));

        verify(sessionService).create(sessionMapper.toEntity(sessionDto));
        verify(sessionMapper, times(2)).toDto(mockSession);
    }

    @Test
    @DisplayName("Update session by ID should return response OK")
    public void update_SessionDtoById_ShouldReturnResponseOk() {
        String id = "1";
        Session mockSession = mock(Session.class);
        SessionDto sessionDto = new SessionDto(
                12L,
                "test",
                new Date(),
                12L,
                "Description of the session here",
                Arrays.asList(1L, 2L, 3L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(sessionService.update(Long.parseLong(id), sessionMapper.toEntity(sessionDto))).thenReturn(mockSession);
        ResponseEntity<?> response = sessionController.update(id, sessionDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionMapper.toDto(mockSession));
        verify(sessionService).update(Long.parseLong(id), sessionMapper.toEntity(sessionDto));
    }

    @Test
    @DisplayName("Update with invalid session ID should return BAD REQUEST")
    public void update_SessionDtoById_ShouldReturnBadRequest() {
        String id = "Invalid ID";
        SessionDto sessionDto = new SessionDto(
                12L,
                "test",
                new Date(),
                12L,
                "Description of the session here",
                Arrays.asList(1L, 2L, 3L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        ResponseEntity<?> response = sessionController.update(id, sessionDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    @DisplayName("Delete session by ID should return response OK")
    public void delete_SessionById_ShouldReturnResponseOk() {
        String id = "1";
        Session mockSession = mock(Session.class);
        when(sessionService.getById(Long.valueOf(id))).thenReturn(mockSession);
        ResponseEntity<?> response = sessionController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).delete(Long.valueOf(id));
    }

    @Test
    @DisplayName("Delete non-existent session should return NOT FOUND")
    public void delete_SessionById_ShouldReturnResponseNotFound() {
        String id = "1";
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);
        ResponseEntity<?> response = sessionController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete session with invalid ID should return BAD REQUEST")
    public void delete_SessionById_ShouldReturnResponseBadRequest() {
        String id = "abc";
        ResponseEntity<?> response = sessionController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService);
    }

    @Test
    @DisplayName("Participate in session should return response OK")
    public void participate_InSession_ShouldReturnResponseOk() {
        String id = "1";
        String userId = "2";
        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).participate(Long.parseLong(id), Long.parseLong(userId));
    }

    @Test
    @DisplayName("Participate in session with invalid IDs should return BAD REQUEST")
    public void participate_InSession_InvalidIds_ShouldReturnBadRequest() {
        String id = "a";
        String userId = "b";
        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService);
    }

    @Test
    @DisplayName("No longer participate in session should return response OK")
    public void noLongerParticipate_InSession_ShouldReturnResponseOk() {
        String id = "1";
        String userId = "2";
        ResponseEntity<?> response = sessionController.noLongerParticipate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).noLongerParticipate(Long.parseLong(id), Long.parseLong(userId));
    }

    @Test
    @DisplayName("No longer participate in session with invalid IDs should return BAD REQUEST")
    public void noLongerParticipate_InSession_InvalidIds_ShouldReturnBadRequest() {
        String id = "a";
        String userId = "b";
        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService);
    }
}
