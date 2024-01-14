package com.openclassrooms.starterjwt.controllersTest.unitTest.controller;


import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Unit Tests for TeacherController")
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
    @DisplayName("Find teacher by ID should return response OK with body")
    public void findById_WithValidId_ShouldReturnResponseOkWithTeacher() {
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
    @DisplayName("Find teacher by ID should return NOT FOUND for non-existent teacher")
    public void findById_WithNonExistentId_ShouldReturnNotFound() {
        String id = "1";

        when(teacherService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verifyNoInteractions(teacherMapper);
    }

    @Test
    @DisplayName("Find teacher by ID with invalid format should return BAD REQUEST")
    public void findById_WithInvalidIdFormat_ShouldReturnBadRequest() {
        String id = "a";
        ResponseEntity<?> response = teacherController.findById(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(teacherMapper, teacherService);
    }

    @Test
    @DisplayName("Get all teachers should return response OK")
    public void findAll_ShouldReturnResponseOkWithListOfTeachers() {
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
