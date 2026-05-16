package com.hexagon.empmanagement.service.impl;

import com.hexagon.empmanagement.dto.EmployeeRequestDTO;
import com.hexagon.empmanagement.dto.EmployeeResponseDTO;
import com.hexagon.empmanagement.entity.Department;
import com.hexagon.empmanagement.entity.Employee;
import com.hexagon.empmanagement.exception.DuplicateEmailException;
import com.hexagon.empmanagement.exception.ResourceNotFoundException;
import com.hexagon.empmanagement.repository.DepartmentRepository;
import com.hexagon.empmanagement.repository.EmployeeRepository;
import com.hexagon.empmanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Employee email is already in use");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setSalary(dto.getSalary());
        employee.setStatus(dto.getStatus());
        employee.setHireDate(java.time.LocalDate.now());
        employee.setDepartment(department);

        Employee saved = employeeRepository.save(employee);
        return mapToResponseDTO(saved);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (!employee.getEmail().equals(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Employee email is already in use");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setSalary(dto.getSalary());
        employee.setStatus(dto.getStatus());
        employee.setDepartment(department);

        Employee updated = employeeRepository.save(employee);
        return mapToResponseDTO(updated);
    }

    @Override
    public void updateEmployeeStatus(Long id, String status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setStatus(status);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setStatus("INACTIVE");
        employeeRepository.save(employee);
    }

    @Override
    public Page<EmployeeResponseDTO> searchEmployees(String name, Long departmentId, String status, Pageable pageable) {
        if (name != null) {
            return employeeRepository.findByFirstNameContainingOrLastNameContaining(name, name, pageable).map(this::mapToResponseDTO);
        } else if (status != null) {
            return employeeRepository.findByStatus(status, pageable).map(this::mapToResponseDTO);
        }
        return employeeRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(Long deptId, Pageable pageable) {
        List<Employee> list = employeeRepository.findByDepartmentId(deptId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Employee> subList = start > list.size() ? List.of() : list.subList(start, end);
        return new PageImpl<>(subList, pageable, list.size()).map(this::mapToResponseDTO);
    }

    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setSalary(employee.getSalary());
        dto.setHireDate(employee.getHireDate());
        dto.setStatus(employee.getStatus());
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        return dto;
    }
}
