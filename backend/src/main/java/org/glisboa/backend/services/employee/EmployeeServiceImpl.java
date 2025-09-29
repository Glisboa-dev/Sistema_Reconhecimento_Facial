package org.glisboa.backend.services.employee;

import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.repositories.employee.EmployeeRepository;
import org.glisboa.backend.dto.employee.CreateEmployeeDTO;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements  EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void createEmployee(CreateEmployeeDTO createEmployeeDTO) {

    }

    @Override
    public void deleteEmployeeByRecordId(Integer recordId) {
        employeeRepository.deleteByRecord_Id(recordId);
    }
}
