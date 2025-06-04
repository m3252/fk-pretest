package com.frankit.productapi.application;

import com.frankit.productapi.domain.Product;
import com.frankit.productapi.domain.ProductRepository;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.ui.dto.ProductCreateRequest;
import com.frankit.productapi.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("상품 서비스 테스트")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("상품을 등록합니다.")
    class TestCreateProduct {

        @Test
        @DisplayName("상품이 정상적으로 등록됩니다.")
        void createProduct() {
            ProductCreateRequest request = new ProductCreateRequest("노트북", "SKU123", "설명", 1200000, 3000);

            Long productId = productService.createProduct(request);

            Product product = productRepository.findById(productId).orElse(null);
            assertThat(product).isNotNull();
            assertThat(product.getName()).isEqualTo("노트북");
        }

        @Test
        @DisplayName("중복된 SKU로 상품을 등록하면 예외가 발생합니다.")
        void createProductWithDuplicateSku() {
            productRepository.save(new Product("모니터", "SKU456", "화면", 300000, 2000));

            ProductCreateRequest request = new ProductCreateRequest("모니터-new", "SKU456", "화면", 300000, 2000);

            assertThatThrownBy(() -> productService.createProduct(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining("Product with the same SKU already exists");

        }
    }

    @Nested
    @DisplayName("상품을 단건 조회합니다.")
    class TestGetProduct {

        @Test
        @DisplayName("존재하는 상품이 조회됩니다.")
        void getProductById() {
            ProductCreateRequest request = new ProductCreateRequest("모니터", "SKU456", "화면", 300000, 2000);
            Long id = productService.createProduct(request);

            ProductResponse response = productService.getProductById(id);

            assertThat(response.name()).isEqualTo("모니터");
        }

        @Test
        @DisplayName("존재하지 않는 상품은 예외가 발생합니다.")
        void getProductByIdFail() {
            assertThatThrownBy(() -> productService.getProductById(9999L))
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("상품을 수정합니다.")
    class TestUpdateProduct {

        @Test
        @DisplayName("상품이 정상적으로 수정됩니다.")
        void updateProduct() {
            ProductCreateRequest request = new ProductCreateRequest("키보드", "SKU789", "기계식", 50000, 1500);
            Long id = productService.createProduct(request);

            ProductCreateRequest updated = new ProductCreateRequest("키보드2", "SKU999", "기계식2", 60000, 2000);
            ProductResponse response = productService.updateProduct(id, updated);

            assertThat(response.name()).isEqualTo("키보드2");
            assertThat(response.price()).isEqualTo(60000);
        }

        @Test
        @DisplayName("중복된 SKU로 상품을 수정하면 예외가 발생합니다.")
        void updateProductWithDuplicateSku() {
            Product saved = productRepository.save(new Product("키보드", "SKU111", "기계식", 50000, 1500));

            ProductCreateRequest updated = new ProductCreateRequest("키보드-new", "SKU111", "무선 마우스3", 30000, 1200);

            assertThatThrownBy(() -> productService.updateProduct(1L, updated))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining("Product with the same SKU already exists");
        }

        @Test
        @DisplayName("같은 상품에 대한 SKU는 중복 예외가 발생하지 않습니다.")
        void updateProductWithSameSku() {

            Product saved = productRepository.save(new Product("키보드", "SKU222", "기계식", 50000, 1500));
            ProductCreateRequest updated = new ProductCreateRequest("키보드-new", "SKU222", "무선 마우스3", 30000, 1200);
            ProductResponse response = productService.updateProduct(saved.getId(), updated);
            assertThat(response.name()).isEqualTo("키보드-new");
        }

    }

    @Nested
    @DisplayName("상품 목록을 페이징 조회합니다.")
    class TestGetProducts {

        @Test
        @DisplayName("상품 목록이 페이징되어 조회됩니다.")
        void getProducts() {
            for (int i = 0; i < 5; i++) {
                productService.createProduct(new ProductCreateRequest("상품" + i, "SKU" + i, "설명", 10000 * i, 500));
            }

            var page = productService.getProducts(org.springframework.data.domain.PageRequest.of(0, 3));

            assertThat(page.getContent().size()).isEqualTo(3);
            assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(5);
        }
    }
}
