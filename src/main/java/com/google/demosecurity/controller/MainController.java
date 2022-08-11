package com.google.demosecurity.controller;

import com.google.demosecurity.entity.Users;
import com.google.demosecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UsersService usersService;

    @GetMapping("/signUp")
    public String signUpPage() {
        return "signUp";
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteAdmin(@PathVariable Long id) {
        Optional<Users> users = usersService.get(id);
        users.ifPresent(usersService::deleteUser);
        return "redirect:/admin";
    }

    @GetMapping("/admin/get/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Users> getAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(usersService.getUsers(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public String userPage() {
        return "user";
    }
}
