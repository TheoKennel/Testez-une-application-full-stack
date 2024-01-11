package com.openclassrooms.starterjwt.controllersTest.unitaire;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
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
@Tag("SessionController_ClassTest")
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
    @Tag("findById_Method_SessionController")
    public void findById_shouldFindSession_ResponseOk() {
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
    @Tag("findById_Method_SessionController")
    public void findById_shouldNotFindSession_NotFound() {
        String id = "1";

        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verifyNoInteractions(sessionMapper);
    }

    @Test
    @Tag("findById_Method_SessionController")
    public void findById_withInvalidId_ShouldReturnBadRequest() {
        String id = "invalid";

        ResponseEntity<?> response = sessionController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    @Tag("findAll_Method_SessionController")
    public void findAll_ResponseOk() {
        Session mockSession = mock(Session.class);
        List<Session> mockSessions = Collections.singletonList(mockSession);
        when(sessionService.findAll()).thenReturn(mockSessions);

        ResponseEntity<?> response = sessionController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionMapper.toDto(mockSessions));
        verify(sessionService).findAll();
    }

    @Test
    @Tag("Create_Method_SessionController")
    public void create_sessionDto_ResponseOk() {
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
    @Tag("Update_SessionDtoById_SessionController")
    public void update_sessionDtoById_ResponseOk() {
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
    @Tag("Update_SessionDtoById_SessionController")
    public void update_sessionDtoById_badRequest() {
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
    @Tag("Save_DeleteSessionById_SessionController")
    public void save_stringId_deleteSession_responseOk() {
        String id = "1";
        Session mockSession = mock(Session.class);
        when(sessionService.getById(Long.valueOf(id))).thenReturn(mockSession);
        ResponseEntity<?> response = sessionController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).delete(Long.valueOf(id));
    }

    @Test
    @Tag("Save_DeleteSessionById_SessionController")
    public void save_stringId_deleteSession_responseNotFound() {
        String id = "1";
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);
        ResponseEntity<?> response = sessionController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Tag("Save_DeleteSessionById_SessionController")
    public void save_stringId_deleteSession_responseBadRequest() {
        String id = "abc";
        ResponseEntity<?> response = sessionController.save(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService);
    }

    @Test
    @Tag("Participate_SessionUser_SessionController")
    public void participate_idUserId_responseOk() {
        String id = "1";
        String userId = "2";
        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).participate(Long.parseLong(id), Long.parseLong(userId));
    }

    @Test
    @Tag("Participate_SessionUser_SessionController")
    public void participate_idUserId_responseBadRequest() {
        String id = "a";
        String userId = "b";
        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService);
    }

    @Test
    @Tag("NoLongerParticipate_SessionUser_SessionController")
    public void noLongerParticipate_idUserId_responseOk() {
        String id = "1";
        String userId = "2";
        ResponseEntity<?> response = sessionController.noLongerParticipate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).noLongerParticipate(Long.parseLong(id), Long.parseLong(userId));
    }

    @Test
    @Tag("NoLongerParticipate_SessionUser_SessionController")
    public void noLongerParticipate_idUserId_responseBadRequest() {
        String id = "a";
        String userId = "b";
        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(sessionService);
    }
}
