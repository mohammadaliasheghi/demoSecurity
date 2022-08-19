package com.google.demosecurity.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtil {

    public String generateToken(String username) {
        String SECRET = "MOHAMMAD_GENERATE_TOKEN_TEST_DEMO_APPLICATION";
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

}
