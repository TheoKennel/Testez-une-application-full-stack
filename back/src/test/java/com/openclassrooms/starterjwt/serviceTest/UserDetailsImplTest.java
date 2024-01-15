package com.openclassrooms.starterjwt.serviceTest;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Tag("UserDetailImpl_Test")
public class UserDetailsImplTest {

    @Test
    public void equals_withSameId_shouldReturnTrue() {
        UserDetailsImpl userDetailsImpl1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetailsImpl2 = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetailsImpl1.equals(userDetailsImpl2)).isTrue();
    }

    @Test
    public void equals_withDifferentId_shouldReturnFalse() {
        UserDetailsImpl userDetailsImpl1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetailsImpl2 = UserDetailsImpl.builder().id(2L).build();

        assertThat(userDetailsImpl1.equals(userDetailsImpl2)).isFalse();
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder().id(1L).build();
        assertThat(userDetailsImpl.equals(null)).isFalse();
    }

    @Test
    public void equals_withDifferentClass_shouldReturnFalse() {
        UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder().id(1L).build();
        Object user2 = new Object();

        assertThat(userDetailsImpl.equals(user2)).isFalse();
    }
}
