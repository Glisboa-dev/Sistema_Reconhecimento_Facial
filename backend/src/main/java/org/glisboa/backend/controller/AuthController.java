package org.glisboa.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.dto.request.auth.LoginRequest;
import org.glisboa.backend.dto.request.employee.AddEmployeeUserRequest;
import org.glisboa.backend.service.auth.AuthService;
import org.glisboa.backend.utils.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success("Login realizado com sucesso", authService.login(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/register/employee")
    public ResponseEntity<ApiResponse> registerEmployee(@RequestBody @Valid AddEmployeeUserRequest request) {
        authService.registerEmployee(request);
        return ApiResponse.success("Funcionário registrado com sucesso", null, HttpStatus.CREATED);
    }
}
