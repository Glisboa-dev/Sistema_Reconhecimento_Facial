package org.glisboa.backend.service.employee;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.domain.repositories.employee.EmployeeRepository;
import org.glisboa.backend.domain.specifications.employee.EmployeeSpecification;
import org.glisboa.backend.dto.request.employee.AddEmployeeRequest;
import org.glisboa.backend.dto.request.employee.filter.SearchEmployeeFilter;
import org.glisboa.backend.dto.response.employee.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepo;
    @Override
    public Employee createEmployee(AddEmployeeRequest request) {
        String cpf = request.cpf();
        if(isCpfValid(cpf)){
            throw new EntityExistsException("Este cpf já está em uso");
        }

        return new Employee(cpf, request.post(), request.description());
    }

    @Override
    public PagedModel<EmployeeResponse> searchEmployees(SearchEmployeeFilter filter, Pageable pageable) {
        Page<Employee> employees = employeeRepo.findAll(
                EmployeeSpecification.filterBy(
                        filter.post(),
                        filter.name(),
                        filter.cpf(),
                        filter.status()
                ),
                pageable
        );

        var dtoList = employees.stream()
                .map(e -> new EmployeeResponse(
                        e.getId(),
                        e.getRecord().getName(),
                        e.getPost(),
                        e.getDescription(),
                        e.getCpf(),
                        e.getRecord().getStatus()
                ))
                .toList();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                pageable.getPageSize(),
                employees.getNumber(),
                employees.getTotalElements(),
                employees.getTotalPages()
        );

        return PagedModel.of(dtoList, metadata);
    }

    private boolean isCpfValid(String cpf){
        return employeeRepo.existsByCpf(cpf);
    }
}
