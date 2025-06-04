package com.frankit.productapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 도메인 테스트")
class ProductTest {

    @Test
    @DisplayName("상품에 옵션을 추가하면 옵션 리스트에 포함된다")
    void testAddOption() {
        Product product = new Product("상품명", "스큐","설명", 1000, 500);
        ProductOption option = new ProductOption("색상", OptionType.SELECT, 0);

        product.addOption(option);

        assertThat(product.getOptions()).contains(option);
    }

    @Test
    @DisplayName("상품은 최대 3개까지 옵션을 가질 수 있다")
    void testMaxOptions() {
        Product product = new Product("상품명", "스큐", "설명", 1000, 500);
        product.addOption(new ProductOption("1", OptionType.INPUT, 0));
        product.addOption(new ProductOption("2", OptionType.INPUT, 0));
        product.addOption(new ProductOption("3", OptionType.INPUT, 0));

        assertThrows(IllegalStateException.class, () ->
                product.addOption(new ProductOption("4", OptionType.INPUT, 0)));
    }
}