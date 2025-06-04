package com.frankit.productapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public User(Long id, String name, String email, String password, boolean deleted, LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public User(String name, String email, String password) {
        this(null, name, email, password, false, LocalDateTime.now(), null);
    }

    public void loggedIn() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void changeMyInformation(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
