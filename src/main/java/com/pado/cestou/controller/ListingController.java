package com.pado.cestou.controller;

import com.pado.cestou.model.Employee;
import com.pado.cestou.model.Listing;
import com.pado.cestou.service.EmployeeService;
import com.pado.cestou.service.ListingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping
    public Listing createListing(@AuthenticationPrincipal Employee seller) {
        Listing listing = new Listing();
        listing.setSeller(seller);
        return listingService.createListing(listing);
    }

    @GetMapping
    public List<Listing> availableListing() {
        return listingService.availableListing();
    }

    @PutMapping("/{id}/book")
    public Listing bookListing(@PathVariable Long id, @AuthenticationPrincipal Employee buyer) {
        return listingService.bookListing(id, buyer);
    }

    @PutMapping("/{id}/cancel-book")
    public Listing cancelBook(@PathVariable Long id, @AuthenticationPrincipal Employee employee) {
        return listingService.cancelBook(id, employee);
    }

    @PutMapping("/{id}/cancel")
    public Listing cancelListing(@PathVariable Long id, @AuthenticationPrincipal Employee employee) {
        return listingService.cancelListing(id, employee);
    }

    @PutMapping("/{id}/conclude")
    public Listing concludedListing(@PathVariable Long id, @AuthenticationPrincipal Employee employee) {
        return listingService.concludedListing(id, employee);
    }

    @GetMapping("/my")
    public List<Listing> getMyListings(@AuthenticationPrincipal Employee employee, @RequestParam String role) {
        if (role.equals("seller")) {
            return listingService.sellerListing(employee);
        } else if (role.equals("buyer")) {
            return listingService.buyerListing(employee);
        }
        throw new RuntimeException("Role inválido. Use 'seller' ou 'buyer'");
    }

}
