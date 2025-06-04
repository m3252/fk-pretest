package com.frankit.productapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 옵션 도메인 테스트")
class ProductOptionTest {

    @Nested
    @DisplayName("선택 타입 옵션인 경우")
    class SelectType {

        @Test
        @DisplayName("옵션 값을 추가할 수 있다.")
        void testAddValue() {
            ProductOption option = new ProductOption("사이즈", OptionType.SELECT, 0);
            option.addOptionValue("M");
            assertThat(option.getValues()).hasSize(1);
        }

        @Test
        @DisplayName("옵션 값을 초기화하고 새로 설정할 수 있다.")
        void testSetValues() {
            ProductOption option = new ProductOption("색상", OptionType.SELECT, 0);
            option.setOptionValues(List.of("빨강", "파랑"));
            assertThat(option.getValues()).extracting("value")
                    .containsExactly("빨강", "파랑");
        }
    }

    @Test
    @DisplayName("입력 타입 옵션은 값을 추가할 수 없다.")
    void testAddToInputType() {
        ProductOption option = new ProductOption("각인", OptionType.INPUT, 0);
        assertThrows(IllegalStateException.class, () -> option.addOptionValue("텍스트"));
    }
}