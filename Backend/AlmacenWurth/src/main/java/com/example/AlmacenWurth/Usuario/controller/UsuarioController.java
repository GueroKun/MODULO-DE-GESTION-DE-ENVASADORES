package com.example.AlmacenWurth.Usuario.controller;

import com.example.AlmacenWurth.Rol.model.Rol;
import com.example.AlmacenWurth.Usuario.model.UsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Solo ADMIN crea usuarios
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO crear(@RequestParam String nombre,
                            @RequestParam String password,
                            @RequestParam Rol rol) {
        return usuarioService.crearUsuario(nombre, password, rol);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }
}

