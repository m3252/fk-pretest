package com.frankit.productapi.domain;


import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;

import java.util.Arrays;

public enum OptionType {
    INPUT("입력 타입"),
    SELECT("선택 타입")
    ;
    private final String description;

    OptionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static OptionType from(String value) {
        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_OPTION_TYPE));
    }
}

