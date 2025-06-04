package com.frankit.productapi.global.security;


import com.frankit.productapi.global.support.JwtTokenValidator;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenValidator validator;

    public JwtAuthenticationProvider(JwtTokenValidator validator) {
        this.validator = validator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        Long userId = validator.validate(token);

        if (userId == null) {
            throw new CustomException(ErrorType.USER_NOT_FOUND_ERROR);
        }

        return new JwtAuthenticationToken(userId, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
