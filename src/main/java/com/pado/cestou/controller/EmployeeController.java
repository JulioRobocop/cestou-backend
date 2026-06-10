package com.pado.cestou.controller;

import com.pado.cestou.model.Employee;
import com.pado.cestou.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    

    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody Employee employee) {
        String token = employeeService.register(employee);
        return Map.of("token", token);
    }


    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> input) {
        int registration = Integer.parseInt(input.get("registration"));
        String password = input.get("password");
        String token = employeeService.login(registration, password);
        return Map.of("token", token);
    }
}
