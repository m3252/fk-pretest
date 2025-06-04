package com.frankit.productapi.global.support;

import com.frankit.productapi.domain.User;
import com.frankit.productapi.domain.UserRepository;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class JwtTokenValidatorTest {

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @MockitoBean
    private UserRepository userRepository;

    @DisplayName("토큰을 생성합니다.")
    @Test
    void generateAndValidateToken() {
        // given
        Long userId = 1L;
        User user = new User(userId, "name", "email", "password", false, LocalDateTime.now(), null);
        given(userRepository.findById(userId)).willReturn(java.util.Optional.of(user));

        // when
        String token = jwtTokenValidator.generateToken(userId);
        Long validatedUserId = jwtTokenValidator.validate(token);

        // then
        assertThat(validatedUserId).isEqualTo(userId);
    }

    @DisplayName("유효하지 않은 토큰을 검증할 때 예외가 발생합니다.")
    @Test
    void invalidTokenThrowsException() {
        // given
        String invalidToken = "invalid.token.value";

        // when & then
        assertThatThrownBy(() -> jwtTokenValidator.validate(invalidToken))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorType.UNAUTHORIZED_ERROR.getMessage());
    }
}
