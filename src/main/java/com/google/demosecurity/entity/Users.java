package com.google.demosecurity.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;
    private String password;
}
