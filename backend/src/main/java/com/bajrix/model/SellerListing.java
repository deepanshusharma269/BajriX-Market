package com.bajrix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seller_listings",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "seller_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "min_order_qty", nullable = false)
    private Integer minOrderQty;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Version
    private Long version; // optimistic lock
}
