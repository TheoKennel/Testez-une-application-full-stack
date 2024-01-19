package com.openclassrooms.starterjwt.mapperImpl;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperImplTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapperImpl;

    @Test
    public void toEntity_WithDtoList_ShouldReturnList() {
        SessionDto session1 = new SessionDto();
        session1.setName("Session 1");
        session1.setDate(new Date());
        session1.setTeacher_id(101L);
        session1.setDescription("Description de la Session 1");

        SessionDto session2 = new SessionDto();
        session2.setName("Session 2");
        session2.setDate(new Date());
        session2.setTeacher_id(102L);
        session2.setDescription("Description de la Session 2");

        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(session1);
        sessionDtoList.add(session2);

        List<Session> sessionsEntity = sessionMapperImpl.toEntity(sessionDtoList);
        assertThat(sessionsEntity.size()).isEqualTo(sessionDtoList.size());
        for (int i = 0; i < sessionsEntity.size(); i++) {
            assertThat(sessionsEntity.get(i).getName()).isEqualTo(sessionDtoList.get(i).getName());
            assertThat(sessionsEntity.get(i).getDate()).isEqualTo(sessionDtoList.get(i).getDate());
        }
    }

    @Test
    public void toEntity_WithNullList_ShouldReturnNull() {
        List<SessionDto> nullSessionDtoList = null;
        List<Session> sessionsEntity = sessionMapperImpl.toEntity(nullSessionDtoList);
        assertThat(sessionsEntity).isNull();
    }

    @Test
    public void toDto_WithDtoList_ShouldReturnList() {
        Teacher teacherOne = mock(Teacher.class);
        Teacher teacherTwo = mock(Teacher.class);

        List<Session> sessionsList = Arrays.asList(
                Session.builder().date(new Date()).description("abc").name("open").teacher(teacherOne).build(),
                Session.builder().date(new Date()).description("abcdef").name("classrooms").teacher(teacherTwo).build()
        );

        List<SessionDto> dtoList = sessionMapperImpl.toDto(sessionsList);
        assertThat(dtoList.size()).isEqualTo(sessionsList.size());
        for(int i = 0; i < dtoList.size(); i++) {
            assertThat(dtoList.get(i).getDescription()).isEqualTo(sessionsList.get(i).getDescription());
            assertThat(dtoList.get(i).getName()).isEqualTo(sessionsList.get(i).getName());
        }
    }

    @Test
    public void toDto_WithNullList_ShouldReturnNull() {
    List<Session> nullSessionList = null;
    List<SessionDto> dtoList = sessionMapperImpl.toDto(nullSessionList);
    assertThat(dtoList).isNull();
    }

    @Test
    public void toEntity_WithDto_ShouldReturnSession() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session 1");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(101L);
        sessionDto.setDescription("Description de la Session 1");

        Session session = sessionMapperImpl.toEntity(sessionDto);
        assertThat(session.getName()).isEqualTo(sessionDto.getName());
        assertThat(session.getDate()).isEqualTo(sessionDto.getDate());
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    public void toEntity_WithDtoNull_ShouldReturnNull() {
        SessionDto sessionDto = null;

        Session session = sessionMapperImpl.toEntity(sessionDto);
        assertThat(session).isNull();
    }

    @Test
    public void toDto_WithEntity_ShouldReturnSession() {
        Teacher teacher = mock(Teacher.class);
        Session session = new Session();
        session.setName("Session 1");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setDescription("Description de la Session 1");

        when(teacher.getId()).thenReturn(123L);

        SessionDto sessionDto = sessionMapperImpl.toDto(session);
        assertThat(session.getName()).isEqualTo(sessionDto.getName());
        assertThat(session.getDate()).isEqualTo(sessionDto.getDate());
        assertThat(sessionDto.getTeacher_id().longValue()).isEqualTo(123L);
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    public void toEntity_WithNullDto_ShouldReturnNull() {
        Session session = null;
        SessionDto sessionDto = sessionMapperImpl.toDto(session);
        assertThat(sessionDto).isNull();
    }
}
