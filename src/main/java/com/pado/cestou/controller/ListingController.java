package com.pado.cestou.controller;

import com.pado.cestou.model.Employee;
import com.pado.cestou.model.Listing;
import com.pado.cestou.service.EmployeeService;
import com.pado.cestou.service.ListingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;
    private final EmployeeService employeeService;

    public ListingController(ListingService listingService, EmployeeService employeeService) {
        this.listingService = listingService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public Listing createListing(@RequestBody Listing listing) {
        return listingService.createListing(listing);
    }

    @GetMapping
    public List<Listing> availableListing() {
        return listingService.availableListing();
    }

    @PutMapping("/{id}/book")
    public Listing bookListing(@PathVariable Long id, @RequestBody Map<String, Long> input) {
        Long buyerId = input.get("employeeId");
        Employee buyer = employeeService.findById(buyerId);
        return listingService.bookListing(id, buyer);
    }

    @PutMapping("/{id}/cancel-book")
    public Listing cancelBook(@PathVariable Long id, @RequestBody Map<String, Long> input) {
        Long employeeId = input.get("employeeId");
        Employee employee = employeeService.findById(employeeId);
        return listingService.cancelBook(id, employee);
    }

    @PutMapping("/{id}/cancel")
    public Listing cancelListing(@PathVariable Long id, @RequestBody Map<String, Long> input) {
        Long employeeId = input.get("employeeId");
        Employee employee = employeeService.findById(employeeId);
        return listingService.cancelListing(id, employee);
    }

    @PutMapping("/{id}/conclude")
    public Listing concludedListing(@PathVariable Long id, @RequestBody Map<String, Long> input) {
        Long employeeId = input.get("employeeId");
        Employee employee = employeeService.findById(employeeId);
        return listingService.concludedListing(id, employee);
    }

}
