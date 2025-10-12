package org.glisboa.backend.controller;

import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.dto.request.employee.filter.SearchEmployeeFilter;
import org.glisboa.backend.service.employee.EmployeeService;
import org.glisboa.backend.utils.api.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchEmployees(
            @RequestParam(required = false) Post post,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) Status status,
            Pageable pageable
    ){
        return ApiResponse.success("Busca realizada com sucesso",
                employeeService.searchEmployees(new SearchEmployeeFilter(post, name, cpf, status), pageable),
                HttpStatus.OK);
    }
}