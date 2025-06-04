package com.frankit.productapi.ui.dto;

public record ProductCreateRequest(String name, String sku, String description, int price, int deliveryFee) {

}
