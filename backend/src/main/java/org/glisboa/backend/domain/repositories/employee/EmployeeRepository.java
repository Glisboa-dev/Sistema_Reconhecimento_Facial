package org.glisboa.backend.domain.repositories.employee;

import org.glisboa.backend.domain.models.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
    boolean existsByCpf(String cpf);
}
