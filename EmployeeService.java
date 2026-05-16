package com.hexagon.empmanagement.service;

import com.hexagon.empmanagement.dto.EmployeeRequestDTO;
import com.hexagon.empmanagement.dto.EmployeeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);
    EmployeeResponseDTO getEmployeeById(Long id);
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);
    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);
    void updateEmployeeStatus(Long id, String status);
    void deleteEmployee(Long id);
    Page<EmployeeResponseDTO> searchEmployees(String name, Long departmentId, String status, Pageable pageable);
    Page<EmployeeResponseDTO> getEmployeesByDepartment(Long deptId, Pageable pageable);
}
