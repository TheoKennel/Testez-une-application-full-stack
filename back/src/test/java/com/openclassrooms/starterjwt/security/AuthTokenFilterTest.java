package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {


    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter = new AuthTokenFilter();


    @Test
    public void doFilterInternal_ValidToken_ShouldAuthenticateUser() throws Exception {
        String fakeJwtToken = "fakeToken";
        String username = "user";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + fakeJwtToken);
        when(jwtUtils.validateJwtToken(fakeJwtToken)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(fakeJwtToken)).thenReturn(username);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        Method doFilterInternal = AuthTokenFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        doFilterInternal.setAccessible(true);
        doFilterInternal.invoke(authTokenFilter, request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken).isTrue();
        assertThat(mockUserDetails).isEqualTo(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
