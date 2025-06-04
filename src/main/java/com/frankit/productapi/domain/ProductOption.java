package com.frankit.productapi.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_options")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private OptionType type;

    private int extraPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionValue> values = new ArrayList<>();

    protected ProductOption() {
    }

    public ProductOption(String name, OptionType type, int extraPrice) {
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
    }

    public ProductOption(String name, OptionType type, int extraPrice, List<ProductOptionValue> values) {
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
        setOptionValues(values.stream().map(ProductOptionValue::getValue).toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OptionType getType() {
        return type;
    }

    public int getExtraPrice() {
        return extraPrice;
    }

    public Product getProduct() {
        return product;
    }

    public List<ProductOptionValue> getValues() {
        return values;
    }

    public void assignToProduct(Product product) {
        this.product = product;
    }

    public void addOptionValue(String value) {
        if (this.type == OptionType.INPUT) {
            throw new IllegalStateException("입력 타입 옵션에는 미리 정의된 값을 추가할 수 없습니다.");
        }

        ProductOptionValue optionValue = new ProductOptionValue(value);
        this.values.add(optionValue);
        optionValue.assignToOption(this);
    }

    public void clearOptionValues() {
        this.values.forEach(value -> value.assignToOption(null));
        this.values.clear();
    }

    public void setOptionValues(List<String> values) {
        clearOptionValues();
        if (values != null && !values.isEmpty()) {
            values.forEach(this::addOptionValue);
        }
    }

    public void update(String name, OptionType type, int extraPrice, List<String> values) {
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
        this.setOptionValues(values);
    }
}