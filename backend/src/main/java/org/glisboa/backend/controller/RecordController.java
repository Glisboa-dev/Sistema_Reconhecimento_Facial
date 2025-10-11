package org.glisboa.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.dto.request.employee.AddEmployeeRequest;
import org.glisboa.backend.dto.request.student.AddStudentRequest;
import org.glisboa.backend.service.record.RecordService;
import org.glisboa.backend.utils.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping(value = "/add-student", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> addStudentRecord(
            @RequestPart("name") @Valid @NotBlank(message = "Nome é obrigatório") String name,
            @RequestPart("student") @Valid AddStudentRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        recordService.addNewStudentRecord(name, file, request);
        return ApiResponse.success("Registro de estudante criado com sucesso", null, HttpStatus.CREATED);
    }

    @PostMapping(value = "/add-employee", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> addEmployeeRecord(
            @RequestPart("name") @Valid @NotBlank(message = "Nome é obrigatório") String name,
            @RequestPart("file") MultipartFile file,
            @RequestPart("employee") @Valid AddEmployeeRequest request
            ) {
        recordService.addNewEmployeeRecord(name, file, request);

        return ApiResponse.success("Registro de funcionário criado com sucesso", null, HttpStatus.CREATED);
    }

    @PutMapping("/update-name/{id}")
    public ResponseEntity<ApiResponse> updateRecordName(@PathVariable Integer id, @RequestParam String newName) {
        recordService.updateRecordName(id, newName);
        return ApiResponse.success("Nome do registro atualizado com sucesso", null, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteRecordById(@PathVariable Integer id) {
        recordService.deleteRecordById(id);
        return ApiResponse.success("Registro deletado com sucesso", null, HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/update-photo/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updateRecordPhoto(@PathVariable Integer id, @RequestPart("file") MultipartFile file) {
        recordService.updateRecordPhoto(id, file);
        return ApiResponse.success("Foto do registro atualizada com sucesso", null, HttpStatus.OK);
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateRecord(@PathVariable Integer id) {
        recordService.deactivateRecord(id);
        return ApiResponse.success("Registro desativado com sucesso", null, HttpStatus.OK);

    }
}