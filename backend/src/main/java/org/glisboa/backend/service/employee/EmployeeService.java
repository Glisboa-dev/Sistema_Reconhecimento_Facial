package org.glisboa.backend.service.employee;

import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.dto.request.employee.AddEmployeeRequest;
import org.glisboa.backend.dto.request.employee.filter.SearchEmployeeFilter;
import org.glisboa.backend.dto.response.employee.EmployeeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface EmployeeService {
    Employee createEmployee(AddEmployeeRequest request);
    PagedModel<EmployeeResponse> searchEmployees(SearchEmployeeFilter filter, Pageable pageable);
}
