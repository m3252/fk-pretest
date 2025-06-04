package com.frankit.productapi.application;

import com.frankit.productapi.domain.*;
import com.frankit.productapi.global.support.error.CustomException;
import com.frankit.productapi.global.support.error.ErrorType;
import com.frankit.productapi.ui.dto.ProductOptionRequest;
import com.frankit.productapi.ui.dto.ProductOptionResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductOptionService {

    private final ProductRepository productRepository;

    public ProductOptionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addOption(Long productId, ProductOptionRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorType.PRODUCT_NOT_FOUND_ERROR));

        ProductOption option = new ProductOption(req.name(), OptionType.from(req.type()), req.extraPrice());
        option.setOptionValues(req.values());
        product.addOption(option);
    }

    public void deleteOption(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorType.PRODUCT_NOT_FOUND_ERROR));

        product.removeOption(optionId);
    }

    public void updateOption(Long productId, Long optionId, ProductOptionRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorType.PRODUCT_NOT_FOUND_ERROR));

        product.updateOption(optionId, req.name(), req.type(), req.extraPrice(), req.values());
    }

    public List<ProductOptionResponse> getOptions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorType.PRODUCT_NOT_FOUND_ERROR));

        if (product.getOptions().isEmpty()) {
            throw new CustomException(ErrorType.PRODUCT_OPTION_NOT_FOUND_ERROR);
        }

        return product.getOptions().stream()
                .map(option -> new ProductOptionResponse(
                        option.getId(),
                        option.getName(),
                        option.getType().name(),
                        option.getExtraPrice(),
                        option.getValues().stream()
                                .map(ProductOptionValue::getValue)
                                .collect(Collectors.toList())
                ))
                .toList();
    }
}
