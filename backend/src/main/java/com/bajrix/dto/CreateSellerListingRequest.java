package com.bajrix.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSellerListingRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Min(0)
    private Double price;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    @Min(1)
    private Integer minOrderQty;
}
