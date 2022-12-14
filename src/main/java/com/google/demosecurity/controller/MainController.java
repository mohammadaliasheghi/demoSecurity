package com.google.demosecurity.controller;

import com.google.demosecurity.entity.Users;
import com.google.demosecurity.jwt.JwtAuth;
import com.google.demosecurity.jwt.JwtUtil;
import com.google.demosecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @GetMapping("/signUp")
    public String signUpPage() {
        return "signUp";
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('OP_ACCESS_ADMIN')")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('OP_DELETE_USER')")
    public String delete(@PathVariable Long id) {
        Optional<Users> users = usersService.get(id);
        users.ifPresent(usersService::deleteUser);
        return "redirect:/admin";
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('OP_GET_USER')")
    public ResponseEntity<Users> get(@PathVariable Long id) {
        return new ResponseEntity<>(usersService.getUsers(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    public String userPage() {
        return "user";
    }

    @GetMapping("/getCookie")
    public String getCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies())
            System.out.println(cookie.getName() + " : " + cookie.getValue());
        return "index";
    }

    @GetMapping("/setCookie")
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("mohammad", "42589");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return "index";
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('OP_PRINCIPAL_INFO')")
    public @ResponseBody
    Principal getPrincipal(Principal principal) {
        return principal;
    }

    @PostMapping("/jwt/login")
    public ResponseEntity<?> jwtLogin(@RequestBody JwtAuth jwtAuth, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuth.getUsername(), jwtAuth.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        response.addHeader("Authorization", jwtUtil.generateToken(jwtAuth.getUsername()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/jwt/hello")
    public ResponseEntity<?> jwtPage() {
        return new ResponseEntity<>("HELLO JWT", HttpStatus.OK);
    }

    @GetMapping("/otp/login")
    public String optLogin() {
        return "otp-username";
    }

    @PostMapping("/otp/check-username")
    public String checkUsername(@ModelAttribute("email") String email, HttpSession httpSession) {
        Users users = (Users) usersService.loadUserByUsername(email);
        Random random = new Random();
        int password;
        if (users == null) {
            users = new Users();
            users.setEmail(email);
        }
        password = random.nextInt(999999);
        System.out.println(password);
        users.setPassword(String.valueOf(password));
        usersService.register(users);

        httpSession.setAttribute("email", email);
        return "redirect:/otp/check-password";
    }

    @GetMapping("/otp/check-password")
    public String otpPassword(HttpSession httpSession, Model model) {
        String email = httpSession.getAttribute("email").toString();
        model.addAttribute("email", email);
        return "otp-password";
    }

}
