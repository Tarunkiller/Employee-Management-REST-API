package com.hexagon.empmanagement.service.impl;

import com.hexagon.empmanagement.dto.DepartmentDTO;
import com.hexagon.empmanagement.entity.Department;
import com.hexagon.empmanagement.exception.ResourceNotFoundException;
import com.hexagon.empmanagement.repository.DepartmentRepository;
import com.hexagon.empmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToDTO(department);
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setManagerName(dto.getManagerName());
        department.setLocation(dto.getLocation());
        
        Department saved = departmentRepository.save(department);
        return mapToDTO(saved);
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        department.setName(dto.getName());
        department.setManagerName(dto.getManagerName());
        department.setLocation(dto.getLocation());

        Department updated = departmentRepository.save(department);
        return mapToDTO(updated);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        long activeEmployees = department.getEmployees().stream()
                .filter(e -> "ACTIVE".equals(e.getStatus()))
                .count();

        if (activeEmployees > 0) {
            throw new IllegalStateException("Cannot delete department with active employees");
        }

        departmentRepository.delete(department);
    }

    private DepartmentDTO mapToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setManagerName(department.getManagerName());
        dto.setLocation(department.getLocation());
        int activeCount = (int) department.getEmployees().stream()
                .filter(e -> "ACTIVE".equals(e.getStatus()))
                .count();
        dto.setEmployeeCount(activeCount);
        return dto;
    }
}
