package com.frankit.productapi.ui.dto;

import com.frankit.productapi.domain.ProductOption;
import com.frankit.productapi.domain.ProductOptionValue;

import java.util.List;

public record ProductOptionResponse (
        Long id,
        String name,
        String type,
        Integer extraPrice,
        List<String> values
) {
    public static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(
                option.getId(),
                option.getName(),
                option.getType().name(),
                option.getExtraPrice(),
                option.getValues().stream().map(ProductOptionValue::toString).toList()
        );
    }
}
