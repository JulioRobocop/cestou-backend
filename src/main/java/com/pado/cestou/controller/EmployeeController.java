package com.pado.cestou.controller;

import com.pado.cestou.model.Employee;
import com.pado.cestou.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/me")
    public Employee getCurrentUser(@AuthenticationPrincipal Employee employee) {
        return employee;
    }

    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody Employee employee, HttpServletResponse response) {
        String token = employeeService.register(employee);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // Change to true when in production (with HTTPS)
                .path("/")
                .maxAge(60 * 60 * 24)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-cookie", cookie.toString());
        return Map.of("message", "registro realizado com sucesso",
                "name", employee.getName(),
                "id", employee.getId().toString()
        );
    }


    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> input, HttpServletResponse response) {
        int registration = Integer.parseInt(input.get("registration"));
        String password = input.get("password");
        String token = employeeService.login(registration, password);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // Change to true when in production (with HTTPS)
                .path("/")
                .maxAge(60 * 60 * 24) // 24 hours
                .sameSite("Lax")
                .build();
        response.addHeader("Set-cookie", cookie.toString());
        Employee found = employeeService.findByRegistration(registration).get();
        return Map.of("message", "Login realizado com sucesso",
                "name", found.getName(),
                "id", found.getId().toString()
        );
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return Map.of("message", "Logout realizado com sucesso");
    }
}
