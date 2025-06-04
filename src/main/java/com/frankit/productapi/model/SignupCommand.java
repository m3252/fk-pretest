package com.frankit.productapi.model;

public record SignupCommand(
        String name,
        String email,
        String password) {

}
