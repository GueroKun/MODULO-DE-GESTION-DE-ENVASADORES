package com.example.AlmacenWurth.Usuario.controller;

import com.example.AlmacenWurth.Rol.model.Rol;
import com.example.AlmacenWurth.Usuario.model.Usuario;
import com.example.AlmacenWurth.Usuario.model.UsuarioDTO;
import com.example.AlmacenWurth.Usuario.model.UsuarioRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDTO crearUsuario(String nombre, String passwordPlano, Rol rol) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("nombre es requerido");
        if (passwordPlano == null || passwordPlano.isBlank()) throw new IllegalArgumentException("password es requerido");
        if (rol == null) throw new IllegalArgumentException("rol es requerido");

        String n = nombre.trim();
        if (usuarioRepository.existsByNombre(n)) throw new IllegalArgumentException("Ya existe el usuario: " + n);

        Usuario u = new Usuario();
        u.setNombre(n);
        u.setPasswordHash(passwordEncoder.encode(passwordPlano));
        u.setRol(rol);

        return toDTO(usuarioRepository.save(u));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorNombreOrThrow(String nombre) {
        return usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + nombre));
    }

    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setRol(u.getRol().name());
        return dto;
    }
}
