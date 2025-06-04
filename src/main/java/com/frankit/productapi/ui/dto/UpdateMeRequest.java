package com.frankit.productapi.ui.dto;

public record UpdateMeRequest(
        String name,
        String email,
        String password) { }
