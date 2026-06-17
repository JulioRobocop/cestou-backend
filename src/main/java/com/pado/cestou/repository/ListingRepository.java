package com.pado.cestou.repository;

import com.pado.cestou.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findBySeller(Employee seller);
    List<Listing> findByBuyer(Employee buyer);
    @Query("SELECT l FROM Listing l WHERE l.status = :status " +
            "AND (:sector IS NULL OR l.seller.sector = :sector) " +
            "AND (:workShift IS NULL OR l.seller.workShift = :workShift)")
    List<Listing> findAvailableWithFilters(
            @Param("status") Status status,
            @Param("sector") Sector sector,
            @Param("workShift")WorkShift workShift
            );
}


