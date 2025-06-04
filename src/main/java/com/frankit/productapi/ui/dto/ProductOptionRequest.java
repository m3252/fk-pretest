package com.frankit.productapi.ui.dto;

import java.util.List;

public record ProductOptionRequest (
        String name,
        String type,
        Integer extraPrice,
        List<String> values
) {}