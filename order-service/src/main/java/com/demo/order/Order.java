package com.demo.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")   // "order" is a reserved word in SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "customerId is required")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotBlank(message = "productName is required")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", message = "price cannot be negative")
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
