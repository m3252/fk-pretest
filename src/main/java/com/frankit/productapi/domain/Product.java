package com.frankit.productapi.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sku;

    @Column(length = 1000)
    private String description;

    private int price;

    private int deliveryFee;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    protected Product() {
    }

    public Product(String name, String sku, String description, int price, int deliveryFee) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<ProductOption> getOptions() {
        return options;
    }

    public void addOption(ProductOption option) {
        if (this.options.size() >= 3) {
            throw new IllegalStateException("상품 옵션은 최대 3개까지만 등록할 수 있습니다.");
        }
        option.assignToProduct(this);
        this.options.add(option);
    }

    public void update(String name, String sku, String description, int price, int deliveryFee) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
    }

    public void removeOption(Long optionId) {
        this.options.removeIf(opt -> opt.getId().equals(optionId));
    }

    public void updateOption(Long optionId, String name, String type, int extraPrice, List<String> values) {
        ProductOption option = this.options.stream()
                .filter(o -> o.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션 없음"));

        option.update(name, OptionType.from(type), extraPrice, values);
    }
}