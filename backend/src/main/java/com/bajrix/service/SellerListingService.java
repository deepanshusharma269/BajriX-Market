package com.bajrix.service;

import com.bajrix.dto.CreateSellerListingRequest;
import com.bajrix.dto.SellerListingDto;
import com.bajrix.model.Product;
import com.bajrix.model.Seller;
import com.bajrix.model.SellerListing;
import com.bajrix.repository.ProductRepository;
import com.bajrix.repository.SellerListingRepository;
import com.bajrix.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerListingService {
    private final SellerListingRepository listingRepo;
    private final ProductRepository productRepo;
    private final SellerRepository sellerRepo;

    @Autowired
    public SellerListingService(SellerListingRepository listingRepo,
                                ProductRepository productRepo,
                                SellerRepository sellerRepo) {
        this.listingRepo = listingRepo;
        this.productRepo = productRepo;
        this.sellerRepo = sellerRepo;
    }

    @Transactional(readOnly = true)
    public List<SellerListingDto> getActiveListingsForProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return listingRepo.findAll().stream()
                .filter(l -> l.getProduct().getId().equals(productId) && l.getIsActive()
                        && l.getSeller().getStatus() == com.bajrix.model.SellerStatus.APPROVED)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SellerListingDto> getMyListings(Long sellerId) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        return listingRepo.findAll().stream()
                .filter(l -> l.getSeller().getId().equals(sellerId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SellerListingDto createListing(Long sellerId, CreateSellerListingRequest req) {
        Seller seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        // Check duplicate
        boolean exists = listingRepo.findAll().stream()
                .anyMatch(l -> l.getSeller().getId().equals(sellerId) && l.getProduct().getId().equals(req.getProductId()));
        if (exists) {
            throw new IllegalArgumentException("Listing already exists for this seller and product");
        }
        SellerListing listing = new SellerListing();
        listing.setSeller(seller);
        listing.setProduct(product);
        listing.setPrice(req.getPrice());
        listing.setStock(req.getStock());
        listing.setMinOrderQty(req.getMinOrderQty());
        listing.setIsActive(true);
        SellerListing saved = listingRepo.save(listing);
        return toDto(saved);
    }

    @Transactional
    public SellerListingDto updateListing(Long sellerId, Long listingId, CreateSellerListingRequest req) {
        SellerListing listing = listingRepo.findById(listingId)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!listing.getSeller().getId().equals(sellerId)) {
            throw new SecurityException("Cannot modify another seller's listing");
        }
        listing.setPrice(req.getPrice());
        listing.setStock(req.getStock());
        listing.setMinOrderQty(req.getMinOrderQty());
        // isActive may be toggled via separate endpoint; keep unchanged here
        SellerListing saved = listingRepo.save(listing);
        return toDto(saved);
    }

    @Transactional
    public void deactivateListing(Long sellerId, Long listingId) {
        SellerListing listing = listingRepo.findById(listingId)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!listing.getSeller().getId().equals(sellerId)) {
            throw new SecurityException("Cannot modify another seller's listing");
        }
        listing.setIsActive(false);
        listingRepo.save(listing);
    }

    private SellerListingDto toDto(SellerListing l) {
        SellerListingDto dto = new SellerListingDto();
        dto.setId(l.getId());
        dto.setProductId(l.getProduct().getId());
        dto.setProductName(l.getProduct().getName());
        dto.setSellerId(l.getSeller().getId());
        dto.setSellerName(l.getSeller().getName());
        dto.setPrice(l.getPrice());
        dto.setStock(l.getStock());
        dto.setMinOrderQty(l.getMinOrderQty());
        dto.setIsActive(l.getIsActive());
        return dto;
    }
}
