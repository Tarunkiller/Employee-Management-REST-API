package com.hexagon.empmanagement.service;

import com.hexagon.empmanagement.dto.EmployeeRequestDTO;
import com.hexagon.empmanagement.dto.EmployeeResponseDTO;
import com.hexagon.empmanagement.entity.Department;
import com.hexagon.empmanagement.entity.Employee;
import com.hexagon.empmanagement.exception.DuplicateEmailException;
import com.hexagon.empmanagement.repository.DepartmentRepository;
import com.hexagon.empmanagement.repository.EmployeeRepository;
import com.hexagon.empmanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void createEmployee_success() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setSalary(new BigDecimal("50000"));
        dto.setStatus("ACTIVE");
        dto.setDepartmentId(1L);

        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(false);

        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));

        Employee saved = new Employee();
        saved.setId(1L);
        saved.setFirstName("John");
        saved.setLastName("Doe");
        saved.setEmail("john@example.com");
        saved.setDepartment(dept);
        
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeResponseDTO response = employeeService.createEmployee(dto);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("IT", response.getDepartmentName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_duplicateEmail_throws() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("john@example.com");

        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> employeeService.createEmployee(dto));
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
