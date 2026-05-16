package com.hexagon.empmanagement.repository;

import com.hexagon.empmanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
    Page<Employee> findByStatus(String status, Pageable pageable);
    Page<Employee> findByFirstNameContainingOrLastNameContaining(String f, String l, Pageable pageable);
    boolean existsByEmail(String email);
}
