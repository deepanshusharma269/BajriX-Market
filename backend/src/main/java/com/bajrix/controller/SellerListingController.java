package com.bajrix.controller;

import com.bajrix.dto.SellerListingDto;
import com.bajrix.dto.CreateSellerListingRequest;
import com.bajrix.service.SellerListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerListingController {

    private final SellerListingService sellerListingService;

    @Autowired
    public SellerListingController(SellerListingService sellerListingService) {
        this.sellerListingService = sellerListingService;
    }

    // Helper to obtain mocked seller id from request attribute (set by filter)
    private Long getSellerId(org.springframework.http.HttpHeaders headers) {
        // Not used; we rely on request attribute via @RequestAttribute
        return null;
    }

    @GetMapping("/listings")
    public ResponseEntity<List<SellerListingDto>> myListings(@RequestAttribute("sellerId") Long sellerId) {
        List<SellerListingDto> listings = sellerListingService.getMyListings(sellerId);
        return ResponseEntity.ok(listings);
    }

    @PostMapping("/listings")
    public ResponseEntity<SellerListingDto> createListing(
            @RequestAttribute("sellerId") Long sellerId,
            @RequestBody CreateSellerListingRequest request) {
        SellerListingDto created = sellerListingService.createListing(sellerId, request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/listings/{id}")
    public ResponseEntity<SellerListingDto> updateListing(
            @RequestAttribute("sellerId") Long sellerId,
            @PathVariable("id") Long listingId,
            @RequestBody CreateSellerListingRequest request) {
        SellerListingDto updated = sellerListingService.updateListing(sellerId, listingId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity<Void> deactivateListing(
            @RequestAttribute("sellerId") Long sellerId,
            @PathVariable("id") Long listingId) {
        sellerListingService.deactivateListing(sellerId, listingId);
        return ResponseEntity.noContent().build();
    }
}
