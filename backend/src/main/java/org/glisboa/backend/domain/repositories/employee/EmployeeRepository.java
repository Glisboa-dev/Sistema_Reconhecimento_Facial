package org.glisboa.backend.domain.repositories.employee;

import org.glisboa.backend.domain.models.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    void deleteByRecord_Id(Integer id);
}
