package com.pado.cestou.repository;

import com.pado.cestou.model.Employee;
import com.pado.cestou.model.Listing;
import com.pado.cestou.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByStatus(Status status);
    List<Listing> findBySeller(Employee seller);
}


