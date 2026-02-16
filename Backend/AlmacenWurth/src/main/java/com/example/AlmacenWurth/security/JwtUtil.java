package com.example.AlmacenWurth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    // Cambia esto en producción y guárdalo como ENV
    private static final String SECRET = "CAMBIA_ESTE_SECRETO_SUPER_LARGO_123456789";
    private static final long EXP_MS = 1000L * 60 * 60 * 8; // 8 horas

    public static String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    public static String extraerUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
