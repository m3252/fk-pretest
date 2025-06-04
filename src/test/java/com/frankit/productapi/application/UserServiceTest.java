package com.frankit.productapi.application;

import com.frankit.productapi.domain.User;
import com.frankit.productapi.domain.UserRepository;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import com.frankit.productapi.model.LoginCommand;
import com.frankit.productapi.model.SignupCommand;
import com.frankit.productapi.model.UserUpdateCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("유저 서비스 테스트")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("회원 가입을 하면 토큰을 반환합니다.")
    class testSignupAndFind {

        @DisplayName("회원 가입에 성공하고 조회할 수 있다.")
        @Test
        void signupAndFindUser() {
            // given
            SignupCommand command = new SignupCommand("testUser", "test@example.com", "password");

            // when
            String token = userService.signup(command);

            // then
            User user = userRepository.findByEmail(command.email());
            assertThat(user).isNotNull();
            assertThat(user.getName()).isEqualTo("testUser");
            assertThat(user.getEmail()).isEqualTo("test@example.com");
            assertThat(user.getPassword()).isNotEqualTo("password");
            assertThat(token).isNotBlank();
        }
    }

    @Nested
    @DisplayName("로그인이 성공하면 토큰을 반환합니다.")
    class testLogin {

        @DisplayName("회원 가입 후 정상 로그인에 성공한다.")
        @Test
        void loginSuccess() {
            // given
            SignupCommand signupCommand = new SignupCommand("testUser", "test@example.com", "password");
            userService.signup(signupCommand);

            // when
            String token = userService.login(new LoginCommand("test@example.com", "password"));

            // then
            assertThat(token).isNotBlank();
        }

        @DisplayName("잘못된 비밀번호는 로그인에 실패한다.")
        @Test
        void loginFailWithWrongPassword() {
            // given
            SignupCommand signupCommand = new SignupCommand("testUser", "test@example.com", "password");
            userService.signup(signupCommand);

            // when & then
            assertThatThrownBy(() -> userService.login(new LoginCommand("test@example.com", "wrongPassword")))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(ErrorType.USER_NOT_FOUND_ERROR.getMessage());
        }
    }

    @Nested
    @DisplayName("회원 정보를 수정합니다.")
    class testUpdateUser {

        @DisplayName("회원 정보 수정에 성공한다.")
        @Test
        void updateUser() {
            // given
            SignupCommand signupCommand = new SignupCommand("testUser", "test@example.com", "password");
            userService.signup(signupCommand);
            User user = userRepository.findByEmail(signupCommand.email());

            UserUpdateCommand updateCommand = new UserUpdateCommand("updatedName", "updated@example.com", "newPassword");

            // when
            User updatedUser = userService.updateUser(user.getId(), updateCommand);

            // then
            assertThat(updatedUser.getName()).isEqualTo("updatedName");
            assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
            assertThat(updatedUser.getPassword()).isNotEqualTo("password");
        }
    }

    @Nested
    @DisplayName("회원 탈퇴합니다.")
    class testDeleteUser {

        @DisplayName("회원 탈퇴에 성공한다.")
        @Test
        void deleteUser() {
            // given
            SignupCommand signupCommand = new SignupCommand("testUser", "test@example.com", "password");
            userService.signup(signupCommand);
            User user = userRepository.findByEmail(signupCommand.email());

            // when
            userService.markUserAsDeleted(user.getId());

            // then
            User deletedUser = userRepository.findById(user.getId()).orElse(null);
            assertThat(deletedUser).isNull();
        }
    }
}
