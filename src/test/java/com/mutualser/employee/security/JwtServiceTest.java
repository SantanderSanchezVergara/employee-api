package com.mutualser.employee.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET =
            "3f7b2c1a9e4d8f6c0b5e2a7d4f1c8b3e6a9d2f5c0e7b4a1d8f3c6e9b2a5d7f0";
    private static final long EXPIRATION = 86400000L; // 24 horas en ms

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
    }

    private UserDetails buildUser(String username) {
        return new User(username, "password_hasheado", List.of());
    }

    @Test
    void generateToken_retornaTokenNoNulo() {
        UserDetails user = buildUser("admin");

        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void extractUsername_retornaElUsernameCorrectoDentroDelToken() {
        UserDetails user = buildUser("admin");
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("admin");
    }

    @Test
    void isTokenValid_cuandoTokenEsValidoYUsuarioCorrecto_retornaTrue() {
        UserDetails user = buildUser("admin");
        String token = jwtService.generateToken(user);

        boolean valid = jwtService.isTokenValid(token, user);

        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_cuandoTokenPerteneceAOtroUsuario_retornaFalse() {
        UserDetails userAdmin = buildUser("admin");
        UserDetails userOtro = buildUser("otro_usuario");
        String tokenDeAdmin = jwtService.generateToken(userAdmin);


        boolean valid = jwtService.isTokenValid(tokenDeAdmin, userOtro);

        assertThat(valid).isFalse();
    }

    @Test
    void generateToken_conDistintosUsuarios_retornaTokensDiferentes() {
        UserDetails user1 = buildUser("alice");
        UserDetails user2 = buildUser("bob");

        String token1 = jwtService.generateToken(user1);
        String token2 = jwtService.generateToken(user2);

        assertThat(token1).isNotEqualTo(token2);
    }
}
