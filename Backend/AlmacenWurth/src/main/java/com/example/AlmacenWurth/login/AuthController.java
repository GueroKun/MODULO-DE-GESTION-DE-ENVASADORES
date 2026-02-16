package com.example.AlmacenWurth.login;

import com.example.AlmacenWurth.Usuario.controller.UsuarioService;
import com.example.AlmacenWurth.Usuario.model.Usuario;
import com.example.AlmacenWurth.login.dto.LoginRequestDTO;
import com.example.AlmacenWurth.login.dto.LoginResponseDTO;
import com.example.AlmacenWurth.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO req) {
        if (req.getNombre() == null || req.getNombre().isBlank()) throw new IllegalArgumentException("nombre requerido");
        if (req.getPassword() == null || req.getPassword().isBlank()) throw new IllegalArgumentException("password requerido");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getNombre().trim(), req.getPassword())
        );

        // Obtener rol desde BD
        Usuario u = usuarioService.obtenerPorNombreOrThrow(req.getNombre().trim());

        String token = JwtUtil.generarToken(u.getNombre(), u.getRol().name());

        LoginResponseDTO res = new LoginResponseDTO();
        res.setToken(token);
        res.setNombre(u.getNombre());
        res.setRol(u.getRol().name());
        return res;
    }
}
