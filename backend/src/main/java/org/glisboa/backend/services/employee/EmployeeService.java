package org.glisboa.backend.services.employee;

import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.dto.employee.CreateEmployeeDTO;

public interface EmployeeService {
    void createEmployee(CreateEmployeeDTO createEmployeeDTO);

    void deleteEmployeeByRecordId(Integer recordId);
}
