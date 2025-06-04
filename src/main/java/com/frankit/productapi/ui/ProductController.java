package com.frankit.productapi.ui;

import com.frankit.productapi.application.ProductService;
import com.frankit.productapi.global.support.api.ApiResponse;
import com.frankit.productapi.ui.dto.ProductCreateRequest;
import com.frankit.productapi.ui.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ApiResponse<Long> createProduct(@RequestBody ProductCreateRequest request) {
        Long productId = productService.createProduct(request);
        return ApiResponse.success(productId);
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponse> products = productService.getProducts(PageRequest.of(page, size));
        return ApiResponse.success(products);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ApiResponse.success(product);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductCreateRequest request
    ) {
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ApiResponse.success(updatedProduct);
    }
}