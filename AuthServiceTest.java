package com.hexagon.empmanagement.service;

import com.hexagon.empmanagement.dto.AuthRequestDTO;
import com.hexagon.empmanagement.dto.AuthResponseDTO;
import com.hexagon.empmanagement.entity.Role;
import com.hexagon.empmanagement.security.JwtUtil;
import com.hexagon.empmanagement.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_success() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("testuser");
        request.setPassword("password");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        UserDetails userDetails = new User("testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("dummy-token");

        AuthResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("dummy-token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertTrue(response.getRoles().contains("ROLE_EMPLOYEE"));
    }

    @Test
    void login_badCredentials_throws() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("testuser");
        request.setPassword("wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
