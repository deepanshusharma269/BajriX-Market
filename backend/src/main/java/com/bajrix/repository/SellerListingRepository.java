package com.bajrix.repository;

import com.bajrix.model.SellerListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerListingRepository extends JpaRepository<SellerListing, Long> {
    // custom query methods can be added as needed
}
