package org.glisboa.backend.services.employee;

import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.domain.repositories.employee.EmployeeRepository;
import org.glisboa.backend.dto.employee.CreateEmployeeDTO;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements  EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        String cpf = createEmployeeDTO.cpf();
        validateCpf(cpf);

        save(new Employee(cpf, createEmployeeDTO.post(), createEmployeeDTO.description()));
    }

    @Override
    public void deleteEmployeeByRecordId(Integer recordId) {
        employeeRepository.deleteByRecord_Id(recordId);
    }

    private void save(Employee employee) {
        employeeRepository.save(employee);
    }
    private void validateCpf(String cpf) {
        if(employeeRepository.existsByCpf(cpf)) {
            throw new RuntimeException("CPF já existe");
        }
    }
}