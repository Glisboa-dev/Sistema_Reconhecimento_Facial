package org.glisboa.backend.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.glisboa.backend.dto.employee.CreateEmployeeDTO;
import org.glisboa.backend.dto.student.CreateStudentDTO;
import org.glisboa.backend.services.record.RecordServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/records")
public class RecordController {
    private final RecordServiceImpl recordService;

    @PostMapping(value = "/create-student", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createStudentRecord(
            @RequestPart("name") @Valid @NotBlank(message = "Nome é obrigatório") String name,
            @RequestPart("student") @Valid CreateStudentDTO createStudentDTO,
            @RequestPart("file")  MultipartFile file
    ) {
        recordService.createStudentRecord(name, createStudentDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/create-employee", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createEmployeeRecord(
            @RequestPart("name") @Valid @NotBlank(message = "Nome é obrigatório") String name,
            @RequestPart("employee") @Valid CreateEmployeeDTO createEmployeeDTO,
            @RequestPart("file") MultipartFile file
    ) {
        recordService.createEmployeeRecord(name, createEmployeeDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update-name/{id}")
    public ResponseEntity<Void> updateRecordName(@PathVariable Integer id, @RequestParam String newName) {
        recordService.updateRecordName(id, newName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRecordById(@PathVariable Integer id) {
        recordService.deleteRecordById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
