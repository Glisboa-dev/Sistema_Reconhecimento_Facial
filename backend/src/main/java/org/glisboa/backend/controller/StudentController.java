package org.glisboa.backend.controller;


import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.student.grade.Grade;
import org.glisboa.backend.dto.request.student.filter.StudentSearchFilter;
import org.glisboa.backend.service.student.StudentService;
import org.glisboa.backend.utils.api.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;


    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(required = false) Grade grade,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Status status,
            Pageable pageable
    ) {

        return ApiResponse.success(
                "Busca realizada com sucesso",
                studentService.searchStudents(new StudentSearchFilter(grade, name, studentId, status), pageable),
                HttpStatus.OK
        );
    }
}