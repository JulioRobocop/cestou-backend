package com.pado.cestou.service;

import com.pado.cestou.model.Employee;
import com.pado.cestou.model.Sector;
import com.pado.cestou.model.WorkShift;
import com.pado.cestou.repository.EmployeeRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public Optional<Employee> findByRegistration(int registration) {
        return employeeRepository.findByRegistration(registration);
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public List<Employee> findBySector(Sector sector) {
        return employeeRepository.findBySector(sector);
    }

    public List<Employee> findByWorkshift(WorkShift workShift) {
        return employeeRepository.findByWorkShift(workShift);
    }

    public Employee register(Employee employee) {
        Optional<Employee> email = employeeRepository.findByEmail(employee.getEmail());
        if (email.isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        Optional<Employee> registration = employeeRepository.findByRegistration(employee.getRegistration());
        if (registration.isPresent()) {
            throw new RuntimeException("Matrícula já cadastrada");
        }

        // Hash da senha
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);

        return employeeRepository.save(employee);
    }

    public String login(int registration, String password) {
        Optional<Employee> employee = employeeRepository.findByRegistration(registration);
        if (employee.isEmpty()) {
            throw new BadCredentialsException("Usuário não encontrado");
        }
        Employee found = employee.get();

        if (!passwordEncoder.matches(password, found.getPassword())) {
            throw new BadCredentialsException("Senha incorreta");
        }
        return jwtService.generateAccessToken(found);
    }

}
