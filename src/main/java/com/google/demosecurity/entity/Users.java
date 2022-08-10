package com.google.demosecurity.entity;

import com.google.demosecurity.enums.RoleEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;
    private String password;
    private Boolean enabled = true;

    @ElementCollection(targetClass = RoleEnum.class)
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "email", referencedColumnName = "email"))
    @Enumerated(EnumType.STRING)
    private List<RoleEnum> roleEnum;
}
