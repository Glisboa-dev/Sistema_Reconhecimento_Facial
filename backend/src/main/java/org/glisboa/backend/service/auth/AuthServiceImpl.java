package org.glisboa.backend.service.auth;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.user.User;
import org.glisboa.backend.domain.models.user.role.Role;
import org.glisboa.backend.domain.repositories.user.UserRepository;
import org.glisboa.backend.dto.request.auth.LoginRequest;
import org.glisboa.backend.dto.request.employee.AddEmployeeUserRequest;
import org.glisboa.backend.dto.response.auth.TokenResponse;
import org.glisboa.backend.service.employee.EmployeeService;
import org.glisboa.backend.service.record.RecordService;
import org.glisboa.backend.service.token.TokenService;
import org.glisboa.backend.utils.domain.repo.RepositoryUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RecordService recordService;

    @Override
    public TokenResponse login(LoginRequest request) {
     try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            var authentication = authenticationManager.authenticate(authenticationToken);
            var user = (User) authentication.getPrincipal();
            var token = tokenService.genToken(user);
            return new TokenResponse(token, tokenService.getTokenClaim(token));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao autenticar usuário: " + e.getMessage());
     }
    }

    @Transactional
    @Override
    public void registerEmployee(AddEmployeeUserRequest userRequest) {
        if(userRepo.existsByUsername(userRequest.username())) throw new EntityExistsException("Este nome de usuário já existe");
        String encodedPassword = new BCryptPasswordEncoder().encode(userRequest.password());
        var record = recordService.getRecordByEmployeeId(userRequest.employeeRecordId());
        RepositoryUtils.saveEntity(userRepo, new User(userRequest.username(), encodedPassword, Role.FUNCIONARIO, record));
    }
}
