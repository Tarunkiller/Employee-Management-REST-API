package com.hexagon.empmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String managerName;
    private String location;
    private Integer employeeCount;
}
