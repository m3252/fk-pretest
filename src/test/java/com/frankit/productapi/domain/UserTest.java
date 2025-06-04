package com.frankit.productapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 도메인에 대한 테스트입니다.")
class UserTest {

    @Nested
    @DisplayName("유저의 탈퇴 상태를 확인합니다.")
    class testDeleted {
        @Test
        @DisplayName("유저가 탈퇴합니다.")
        void test1() {
            User user = new User(1L, "name", "email", "password", false, LocalDateTime.now(), null);
            user.deleted();
            assertThat(user.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("탈퇴하지 않은 상태입니다.")
        void test2() {
            User user = new User(1L, "name", "email", "password", false, LocalDateTime.now(), null);
            assertThat(user.isDeleted()).isFalse();
        }
    }

    @DisplayName("유저의 정보를 변경합니다.")
    @Test
    void testChangeMyInformation() {
        User user = new User(1L, "name", "email", "password", false, LocalDateTime.now(), null);
        user.changeMyInformation("newName", "newEmail");

        assertThat(user.getName()).isEqualTo("newName");
        assertThat(user.getEmail()).isEqualTo("newEmail");
    }

    @DisplayName("유저의 비밀번호를 변경합니다.")
    @Test
    void testChangePassword() {
        User user = new User(1L, "name", "email", "password", false, LocalDateTime.now(), null);
        user.changePassword("newPassword");

        assertThat(user.getPassword()).isEqualTo("newPassword");
    }

    @DisplayName("유저의 로그인 시간을 기록합니다.")
    @Test
    void testLoggedIn() {
        User user = new User(1L, "name", "email", "password", false, LocalDateTime.now(), null);
        user.loggedIn();

        assertThat(user.getLastLoginAt()).isNotNull();
        assertThat(user.getLastLoginAt()).isBefore(LocalDateTime.now());
    }
}