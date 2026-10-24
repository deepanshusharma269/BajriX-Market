package com.bajrix.dto;

import lombok.Data;

@Data
public class SellerListingDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long sellerId;
    private String sellerName;
    private Double price;
    private Integer stock;
    private Integer minOrderQty;
    private Boolean isActive;
}
