package com.pado.cestou.repository;

import com.pado.cestou.model.Employee;
import com.pado.cestou.model.Sector;
import com.pado.cestou.model.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByRegistration(int registration);
    List<Employee> findBySector(Sector sector);
    List<Employee> findByWorkShift(WorkShift workShift);
    Optional<Employee> findByEmail(String email);
}
