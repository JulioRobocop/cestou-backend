package com.pado.cestou.controller;

import com.pado.cestou.model.Employee;
import com.pado.cestou.model.Listing;
import com.pado.cestou.service.EmployeeService;
import com.pado.cestou.service.ListingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ListingService listingService;

    public EmployeeController(EmployeeService employeeService, ListingService listingService) {
        this.employeeService = employeeService;
        this.listingService = listingService;
    }
    

    @PostMapping("/register")
    public Employee registerUser(@RequestBody Employee employee) {
        return employeeService.register(employee);
    }


    @PostMapping("/login")
    public Employee loginUser(@RequestBody Map<String, String> input) {
        int registration = Integer.parseInt(input.get("registration"));
        String password = input.get("password");
        return employeeService.login(registration, password);
    }
    
    @GetMapping("/{id}/listings")
    public List<Listing> sellerListing(@PathVariable Long id) {
        Employee employeeListings = employeeService.findById(id);
        return listingService.sellerListing(employeeListings);
    }


}
