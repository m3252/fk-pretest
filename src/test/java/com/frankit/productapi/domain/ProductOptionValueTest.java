package com.frankit.productapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("상품 옵션 값 도메인 테스트")
class ProductOptionValueTest {

    @Test
    @DisplayName("옵션 값이 생성되고 옵션에 할당될 수 있다")
    void testAssign() {
        ProductOption option = new ProductOption("색상", OptionType.SELECT, 0);
        ProductOptionValue value = new ProductOptionValue("검정");
        value.assignToOption(option);

        assertThat(value.getOption()).isEqualTo(option);
    }
}
