package org.glisboa.backend.domain.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.user.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USUARIO")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class User extends EntityModel implements UserDetails {

    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "senha")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "papel")
    private Role role;

    @OneToOne(optional = true)
    @JoinColumn(name = "id_cadastro", nullable = true)
    private Record record;

    public User(String username, String encodedPassword, Role role) {
        super();
        this.username = username;
        this.password = encodedPassword;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == Role.ADMIN || this.role == Role.DBA) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_DBA"),
                new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
        else return  List.of(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
