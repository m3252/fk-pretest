package com.frankit.productapi.ui;

import com.frankit.productapi.application.ProductOptionService;
import com.frankit.productapi.global.support.api.ApiResponse;
import com.frankit.productapi.ui.dto.ProductOptionRequest;
import com.frankit.productapi.ui.dto.ProductOptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/options")
@RequiredArgsConstructor
public class ProductOptionController {
    private final ProductOptionService optionService;

    @PostMapping
    public ApiResponse<?> addOption(@PathVariable Long productId, @RequestBody @Valid ProductOptionRequest request) {
        optionService.addOption(productId, request);
        return ApiResponse.success();
    }

    @PutMapping("/{optionId}")
    public ApiResponse<?> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody @Valid ProductOptionRequest request) {
        optionService.updateOption(productId, optionId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{optionId}")
    public ApiResponse<?> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ApiResponse.success();
    }

    @GetMapping
    public ApiResponse<List<ProductOptionResponse>> getOptions(@PathVariable Long productId) {
        return ApiResponse.success(optionService.getOptions(productId));
    }
}
