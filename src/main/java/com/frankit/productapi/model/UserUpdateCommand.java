package com.frankit.productapi.model;

public record UserUpdateCommand(String name, String email, String password) {
}
