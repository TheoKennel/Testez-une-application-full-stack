package com.openclassrooms.starterjwt.mapperImpl;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;


public class TeacherMapperImplTest {

    TeacherMapperImpl teacherMapperImpl;

    @BeforeEach
    public void setup() {
        teacherMapperImpl = new TeacherMapperImpl();
    }

    @Test
    public void toEntity_WithDto_ShouldReturnTeacher() {
        TeacherDto teacherDto = mock(TeacherDto.class);
        teacherDto.setId(1L);
        teacherDto.setFirstName("OpenClass");
        teacherDto.setLastName("rooms");

        Teacher teacher = teacherMapperImpl.toEntity(teacherDto);

        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());

    }

    @Test
    public void toEntity_WithNullDto_ShouldReturnNull() {
        TeacherDto teacherDto = null;

        Teacher teacher = teacherMapperImpl.toEntity(teacherDto);
        assertThat(teacher).isNull();
    }

    @Test
    public void toDto_WithEntity_ShouldReturnTeacher() {
        Teacher teacher = mock(Teacher.class);
        teacher.setId(1L);
        teacher.setFirstName("OpenClass");
        teacher.setLastName("rooms");

        TeacherDto teacherDto = teacherMapperImpl.toDto(teacher);

        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
    }

    @Test
    public void toDto_WithNullEntity_ShouldReturnNull() {
        Teacher teacher = null;

        TeacherDto teacherDto = teacherMapperImpl.toDto(teacher);
        assertThat(teacherDto).isNull();
    }

    @Test
    public void toDto_WithListEntity_ShouldReturnListTeacher() {
        Teacher teacher = mock(Teacher.class);
        teacher.setId(1L);
        teacher.setFirstName("OpenClass");
        teacher.setLastName("rooms");

        Teacher teacherTwo = mock(Teacher.class);
        teacherTwo.setId(2L);
        teacherTwo.setFirstName("Theo");
        teacherTwo.setLastName("Student");

        List<Teacher> teachersList = new ArrayList<>();
        teachersList.add(teacher);
        teachersList.add(teacherTwo);

        List<TeacherDto> teacherDto = teacherMapperImpl.toDto(teachersList);

        assertThat(teacherDto.size()).isEqualTo(teachersList.size());
        for (int i = 0; i < teacherDto.size(); i++) {
            assertThat(teacherDto.get(i).getFirstName()).isEqualTo(teachersList.get(i).getFirstName());
            assertThat(teacherDto.get(i).getLastName()).isEqualTo(teachersList.get(i).getLastName());
            assertThat(teacherDto.get(i).getId()).isEqualTo(teachersList.get(i).getId());
        }
    }

    @Test
    public void toDto_WithNullListEntity_ShouldReturnListNull() {
        List<Teacher> teacherList = null;
        List<TeacherDto> teacherDto = teacherMapperImpl.toDto(teacherList);

        assertThat(teacherDto).isNull();
    }

    @Test
    public void toEntity_WithListDto_ShouldReturnListTeacher() {
        TeacherDto teacher = mock(TeacherDto.class);
        teacher.setId(1L);
        teacher.setFirstName("OpenClass");
        teacher.setLastName("rooms");

        TeacherDto teacherTwo = mock(TeacherDto.class);
        teacherTwo.setId(2L);
        teacherTwo.setFirstName("Theo");
        teacherTwo.setLastName("Student");

        List<TeacherDto> teacherDto = new ArrayList<>();
        teacherDto.add(teacher);
        teacherDto.add(teacherTwo);

        List<Teacher> teachersList = teacherMapperImpl.toEntity(teacherDto);

        assertThat(teacherDto.size()).isEqualTo(teachersList.size());
        for (int i = 0; i < teacherDto.size(); i++) {
            assertThat(teacherDto.get(i).getFirstName()).isEqualTo(teachersList.get(i).getFirstName());
            assertThat(teacherDto.get(i).getLastName()).isEqualTo(teachersList.get(i).getLastName());
            assertThat(teacherDto.get(i).getId()).isEqualTo(teachersList.get(i).getId());
        }
    }

    @Test
    public void toEntity_WithNullListDto_ShouldReturnNull() {
        List<TeacherDto> teacherDtoList = null;
        List<Teacher> teacherList = teacherMapperImpl.toEntity(teacherDtoList);
        assertThat(teacherList).isNull();
    }
}
