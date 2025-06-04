package com.frankit.productapi.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "product_option_values")
public class ProductOptionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption option;

    protected ProductOptionValue() {
    }

    public ProductOptionValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public ProductOption getOption() {
        return option;
    }

    public void assignToOption(ProductOption option) {
        this.option = option;
    }
}