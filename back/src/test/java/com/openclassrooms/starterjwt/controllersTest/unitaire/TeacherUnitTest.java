package com.openclassrooms.starterjwt.controllersTest.unitaire;


import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("TeacherController_ClassTest")
public class TeacherUnitTest {

    @Mock
    TeacherMapper teacherMapper;
    @Mock
    TeacherService teacherService;
    TeacherController teacherController;

    @BeforeEach
    public void setup() {
        this.teacherController = new TeacherController(teacherService, teacherMapper);
    }

    @Test
    @Tag("FindById_TeacherController")
    public void findById_id_ResponseOkBodyTeacher() {
        String id = "1";
        Teacher mockTeacher = mock(Teacher.class);

        when(teacherService.findById(Long.valueOf(id))).thenReturn(mockTeacher);

        ResponseEntity<?> response = teacherController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(teacherMapper.toDto(mockTeacher));

        verify(teacherService).findById(Long.valueOf(id));
        verify(teacherMapper, times(2)).toDto(mockTeacher);
    }

    @Test
    @Tag("FindById_TeacherController")
    public void findById_id_ResponseNotFound() {
        String id = "1";

        when(teacherService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verifyNoInteractions(teacherMapper);
    }

    @Test
    @Tag("FindById_TeacherController")
    public void findById_id_ResponseBadRequest() {
        String id = "a";
        ResponseEntity<?> response = teacherController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(teacherMapper, teacherService);
    }

    @Test
    @Tag("FindAll_TeacherController")
    public void findAll_ResponseOk() {
        Teacher mockTeacher = mock(Teacher.class);
        List<Teacher> teachers = Collections.singletonList(mockTeacher);

        when(teacherService.findAll()).thenReturn(teachers);

        ResponseEntity<?> response = teacherController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(teacherMapper.toDto(teachers));

        verify(teacherService).findAll();
        verify(teacherMapper, times(2)).toDto(teachers);
    }
}
