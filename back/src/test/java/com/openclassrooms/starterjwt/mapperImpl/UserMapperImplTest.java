package com.openclassrooms.starterjwt.mapperImpl;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserMapperImplTest {

    private UserMapper userMapper = new UserMapperImpl();

    @Test
    public void toEntity_WithUserDto_ShouldReturnNewUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("openclassroomsTest@gmail.com");
        userDto.isAdmin();
        userDto.setFirstName("Class");
        userDto.setLastName("Open");
        userDto.setPassword("123456");

        User user = userMapper.toEntity(userDto);

        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    public void toDto_WithUserEntity_ShouldReturnNewUserDto() {
        User user = new User();
        user.setEmail("openclassroomsTest@gmail.com");
        user.isAdmin();
        user.setFirstName("Class");
        user.setLastName("Open");
        user.setPassword("123456");

        UserDto userDto = userMapper.toDto(user);

        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    public void toDto_withValidList_shouldReturnUserDtoList() {
        List<User> userList = Arrays.asList(
                User.builder().id(1L).email("email1@example.com").lastName("lastName1").firstName("firstName1")
                        .admin(true).password("password1").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build(),
                User.builder().id(2L).email("email2@example.com").lastName("lastName2").firstName("firstName2")
                        .admin(false).password("password2").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()
        );

        List<UserDto> dtoList = userMapper.toDto(userList);

        assertThat(userList.size()).isEqualTo(dtoList.size());
        for (int i = 0; i < userList.size(); i++) {
            assertThat(userList.get(i).getId()).isEqualTo(dtoList.get(i).getId());
            assertThat(userList.get(i).getEmail()).isEqualTo(dtoList.get(i).getEmail());
        }
    }

    @Test
    public void toDto_withNullList_shouldReturnNull() {
        List<User> nullUserList = null;
        List<UserDto> result = userMapper.toDto(nullUserList);
        assertThat(result).isNull();
    }

    @Test
    public void toEntity_withNullList_shouldReturnNull() {
        List<UserDto> nullUserDtoList = null;
        List<User> result = userMapper.toEntity(nullUserDtoList);
        assertThat(result).isNull();
    }

    @Test
    public void toEntity_withValidList_shouldReturnUserDtoList() {
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(1L, "email1@example.com", "lastName1", "firstName1",
                true, "password1", LocalDateTime.now(), LocalDateTime.now()));
        userDtoList.add(new UserDto(2L, "email2@example.com", "lastName2", "firstName2",
                false, "password2", LocalDateTime.now(), LocalDateTime.now()));

        List<User> userList = userMapper.toEntity(userDtoList);

        assertThat(userList.size()).isEqualTo(userDtoList.size());
        for (int i = 0; i < userList.size(); i++) {
            assertThat(userList.get(i).getId()).isEqualTo(userDtoList.get(i).getId());
            assertThat(userList.get(i).getEmail()).isEqualTo(userDtoList.get(i).getEmail());
        }
    }
}
