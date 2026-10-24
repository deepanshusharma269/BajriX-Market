package com.bajrix.controller;

import com.bajrix.dto.ProductDto;
import com.bajrix.dto.SellerListingDto;
import com.bajrix.model.Product;
import com.bajrix.service.ProductService;
import com.bajrix.service.SellerListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final SellerListingService sellerListingService;

    @Autowired
    public ProductController(ProductService productService, SellerListingService sellerListingService) {
        this.productService = productService;
        this.sellerListingService = sellerListingService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> search(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Product> result = productService.search(query, page, size);
        Page<ProductDto> dtoPage = result.map(p -> {
            ProductDto dto = new ProductDto();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setCategory(p.getCategory());
            return dto;
        });
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getById(@PathVariable Long id) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Product not found"));
        List<SellerListingDto> listings = sellerListingService.getActiveListingsForProduct(id);
        ProductDetailResponse resp = new ProductDetailResponse();
        resp.setId(product.getId());
        resp.setName(product.getName());
        resp.setDescription(product.getDescription());
        resp.setCategory(product.getCategory());
        resp.setListings(listings);
        return ResponseEntity.ok(resp);
    }

    // DTO for product detail plus listings
    public static class ProductDetailResponse {
        private Long id;
        private String name;
        private String description;
        private String category;
        private List<SellerListingDto> listings;

        // getters & setters
        public Long getId() {return id;}
        public void setId(Long id) {this.id = id;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getDescription() {return description;}
        public void setDescription(String description) {this.description = description;}
        public String getCategory() {return category;}
        public void setCategory(String category) {this.category = category;}
        public List<SellerListingDto> getListings() {return listings;}
        public void setListings(List<SellerListingDto> listings) {this.listings = listings;}
    }
}
