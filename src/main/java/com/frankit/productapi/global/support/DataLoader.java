package com.frankit.productapi.global.support;

import com.frankit.productapi.domain.OptionType;
import com.frankit.productapi.domain.Product;
import com.frankit.productapi.domain.ProductOption;
import com.frankit.productapi.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        Product product = new Product("노트북", "RA222TTE",  "고성능 게이밍 노트북입니다", 1200000, 3000);

        ProductOption colorOption = new ProductOption("색상", OptionType.SELECT, 0);
        colorOption.addOptionValue("블랙");
        colorOption.addOptionValue("화이트");

        ProductOption ramOption = new ProductOption("RAM 용량", OptionType.SELECT, 10000);
        ramOption.addOptionValue("8GB");
        ramOption.addOptionValue("16GB");

        product.addOption(colorOption);
        product.addOption(ramOption);

        productRepository.save(product);
    }
}
