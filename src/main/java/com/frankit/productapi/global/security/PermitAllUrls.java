package com.frankit.productapi.global.security;

import java.util.List;

public final class PermitAllUrls {
    public static final List<String> URLS = List.of(
            "/users/signup",
            "/users/login"
    );
}
