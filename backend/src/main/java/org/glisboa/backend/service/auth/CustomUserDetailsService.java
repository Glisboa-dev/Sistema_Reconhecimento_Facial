package org.glisboa.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.repositories.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        return user;
    }
}
