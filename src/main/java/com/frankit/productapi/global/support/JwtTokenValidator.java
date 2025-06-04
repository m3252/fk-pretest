package com.frankit.productapi.global.support;

import com.frankit.productapi.domain.User;
import com.frankit.productapi.domain.UserRepository;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenValidator {

    private static final String SECRET = "fk-secret-key-aaaa-bbbbbbbb-ccccccccc";
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24;

    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final UserRepository userRepository;

    public JwtTokenValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public Long validate(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.getSubject());
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new CustomException(ErrorType.USER_NOT_FOUND_ERROR));

            return (!user.isDeleted()) ? user.getId() : null;

        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ERROR);
        }
    }
}