package org.glisboa.backend.services.employee;

import org.glisboa.backend.dto.employee.CreateEmployeeDTO;

public interface EmployeeService {
    void createEmployee(CreateEmployeeDTO createEmployeeDTO);
}
