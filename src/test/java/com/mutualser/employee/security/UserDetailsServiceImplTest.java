package com.mutualser.employee.security;

import com.mutualser.employee.user.model.User;
import com.mutualser.employee.user.model.enumeration.RoleEnum;
import com.mutualser.employee.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_cuandoUsuarioExiste_retornaUserDetailsConRolCorrecto() {
        User user = new User(1L, "admin", "$2a$10$hash_bcrypt", RoleEnum.ADMIN);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("admin");

        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getPassword()).isEqualTo("$2a$10$hash_bcrypt");
        assertThat(result.getAuthorities())
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Test
    void loadUserByUsername_cuandoNoExisteUsuario_lanzaUsernameNotFoundException() {
        when(userRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("desconocido"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("desconocido");
    }

    @Test
    void loadUserByUsername_cuandoRolEsEmployee_retornaAutoridadROLE_EMPLOYEE() {
        User user = new User(2L, "juan", "$2a$10$hash", RoleEnum.EMPLOYEE);
        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("juan");

        assertThat(result.getAuthorities())
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
    }
}
