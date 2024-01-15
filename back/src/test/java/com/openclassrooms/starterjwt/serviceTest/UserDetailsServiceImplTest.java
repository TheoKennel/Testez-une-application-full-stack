package com.openclassrooms.starterjwt.serviceTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void loadUserByUsername_WithUsername_ShouldReturnUserDetailsInfo() {
        String username = "openclassrooms@gmail.com";
        User mockUser = mock(User.class);

        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn(username);
        when(mockUser.getLastName()).thenReturn("LastName");
        when(mockUser.getFirstName()).thenReturn("FirstName");
        when(mockUser.getPassword()).thenReturn("password");
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        assertThat(userDetails.getUsername()).isEqualTo(username);
    }

    @Test
    public void loadUserByUsername_WithInvalidUsername_ShouldThrowException() {
        String invalidUsername = "invalid@gmail.com";
        when(userRepository.findByEmail(invalidUsername)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            userDetailsServiceImpl.loadUserByUsername(invalidUsername);
        }).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: " + invalidUsername);
    }
}
