package com.google.demosecurity.entity;

import com.google.demosecurity.enums.Authority;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
public class Users implements Serializable, UserDetails, OAuth2User {

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

    private String name;
    private String picture;

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (roles != null) {
            for (Role role : roles)
                grantedAuthorities.addAll(role.getAuthorities());
        } else {
            grantedAuthorities.add(Authority.OP_ACCESS_USER);
        }
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

    @Override
    public String getName() {
        return null;
    }
}
