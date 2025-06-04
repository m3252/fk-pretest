package com.frankit.productapi.application;

import com.frankit.productapi.domain.OptionType;
import com.frankit.productapi.domain.Product;
import com.frankit.productapi.domain.ProductRepository;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import com.frankit.productapi.ui.dto.ProductOptionRequest;
import com.frankit.productapi.ui.dto.ProductOptionResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("상품 옵션 서비스 테스트")
class ProductOptionServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductOptionService optionService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("상품 옵션을 추가합니다.")
    void addOption() {
        Product product = new Product("노트북", "SKU-003", "테스트 상품", 100000, 2000);
        productRepository.save(product);

        ProductOptionRequest req = new ProductOptionRequest(
                "색상",
                OptionType.SELECT.name(),
                0,
                List.of("블랙", "화이트")
        );

        optionService.addOption(product.getId(), req);
        entityManager.flush();

        List<ProductOptionResponse> options = optionService.getOptions(product.getId());
        assertThat(options).hasSize(1);
        assertThat(options.get(0).name()).isEqualTo("색상");
    }

    @Test
    @DisplayName("상품 옵션을 수정합니다.")
    void updateOption() {
        Product product = new Product("노트북", "SKU-004", "테스트 상품", 100000, 2000);
        productRepository.save(product);

        ProductOptionRequest createReq = new ProductOptionRequest("색상", OptionType.SELECT.name(), 0, List.of("블랙"));
        optionService.addOption(product.getId(), createReq);
        entityManager.flush();

        Long optionId = optionService.getOptions(product.getId()).get(0).id();

        ProductOptionRequest updateReq = new ProductOptionRequest("색상2", OptionType.SELECT.name(), 500, List.of("레드", "블루"));
        optionService.updateOption(product.getId(), optionId, updateReq);

        List<ProductOptionResponse> updated = optionService.getOptions(product.getId());
        assertThat(updated.get(0).name()).isEqualTo("색상2");
        assertThat(updated.get(0).values()).containsExactly("레드", "블루");
    }

    @Test
    @DisplayName("상품 옵션을 삭제합니다.")
    void deleteOption() {
        Product product = new Product("노트북", "SKU-005", "테스트 상품", 100000, 2000);
        productRepository.save(product);

        ProductOptionRequest createReq = new ProductOptionRequest("색상", OptionType.SELECT.name(), 0, List.of("블랙"));
        optionService.addOption(product.getId(), createReq);
        entityManager.flush();

        Long optionId = optionService.getOptions(product.getId()).get(0).id();
        optionService.deleteOption(product.getId(), optionId);

        assertThatThrownBy(() -> optionService.getOptions(product.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorType.PRODUCT_OPTION_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("옵션이 없을 때 조회 시 예외를 던집니다.")
    void getOptions_noOptions() {
        Product product = new Product("노트북", "SKU-006", "테스트 상품", 100000, 2000);
        productRepository.save(product);

        assertThatThrownBy(() -> optionService.getOptions(product.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorType.PRODUCT_OPTION_NOT_FOUND_ERROR.getMessage());
    }
}
