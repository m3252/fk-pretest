package com.frankit.productapi.ui.dto;

import java.time.LocalDateTime;

public record ProductResponse(Long id, String name, String description, int price, int deliveryFee, LocalDateTime createdAt) {

    public static ProductResponse from(Long id, String name, String description, int price, int deliveryFee, LocalDateTime createdAt) {
        return new ProductResponse(id, name, description, price, deliveryFee, createdAt);
    }
}
