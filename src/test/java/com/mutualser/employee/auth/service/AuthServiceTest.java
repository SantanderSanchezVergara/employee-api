package com.mutualser.employee.auth.service;

import com.mutualser.employee.auth.dto.AuthRequest;
import com.mutualser.employee.auth.dto.AuthResponse;
import com.mutualser.employee.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_cuandoCredencialesValidas_retornaTokenEnRespuesta() {
        AuthRequest request = new AuthRequest("admin", "admin123*");
        UserDetails userDetails = new User("admin", "hashed", List.of());
        String expectedToken = "jwt.token.generado";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // el retorno no se usa en AuthService
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        AuthResponse response = authService.authenticate(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(expectedToken);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("admin");
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void authenticate_cuandoCredencialesInvalidas_lanzaBadCredentialsException() {
        AuthRequest request = new AuthRequest("admin", "password_incorrecto");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");


        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }
}
