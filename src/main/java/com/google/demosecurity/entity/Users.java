package com.google.demosecurity.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Users implements Serializable, UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;
    private String password;
    private Boolean enabled = true;

//    @ElementCollection(targetClass = RoleEnum.class, fetch = FetchType.EAGER)
//    @CollectionTable(
//            name = "authorities",
//            joinColumns = @JoinColumn(name = "email", referencedColumnName = "email"))
//    @Enumerated(EnumType.STRING)
//    private List<RoleEnum> roleEnum;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roles)
            grantedAuthorities.addAll(role.getAuthorities());
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}
