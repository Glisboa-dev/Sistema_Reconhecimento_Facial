package org.glisboa.backend.service.auth;

import org.glisboa.backend.dto.request.auth.LoginRequest;
import org.glisboa.backend.dto.request.employee.AddEmployeeUserRequest;
import org.glisboa.backend.dto.response.auth.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    void registerEmployee(AddEmployeeUserRequest request);
}
