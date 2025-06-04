package com.frankit.productapi.application;

import com.frankit.productapi.domain.Product;
import com.frankit.productapi.domain.ProductRepository;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.ui.dto.ProductCreateRequest;
import com.frankit.productapi.ui.dto.ProductResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.frankit.productapi.global.support.error.ErrorType.PRODUCT_DUPLICATE_ERROR;
import static com.frankit.productapi.global.support.error.ErrorType.PRODUCT_NOT_FOUND_ERROR;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new CustomException(PRODUCT_DUPLICATE_ERROR);
        }

        Product product = new Product(
                request.name(),
                request.sku(),
                request.description(),
                request.price(),
                request.deliveryFee()
        );
        productRepository.save(product);
        return product.getId();
    }

    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> ProductResponse.from(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getDeliveryFee(),
                        product.getCreatedAt()
                ));
    }

    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> ProductResponse.from(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getDeliveryFee(),
                        product.getCreatedAt()
                ))
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND_ERROR));
    }

    public ProductResponse updateProduct(Long id, ProductCreateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND_ERROR));

        if (productRepository.existsBySkuAndIdNot(request.sku(), id)) {
            throw new CustomException(PRODUCT_DUPLICATE_ERROR);
        }

        product.update(
                request.name(),
                request.sku(),
                request.description(),
                request.price(),
                request.deliveryFee()
        );

        productRepository.save(product);

        return ProductResponse.from(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDeliveryFee(),
                product.getCreatedAt()
        );
    }
}
