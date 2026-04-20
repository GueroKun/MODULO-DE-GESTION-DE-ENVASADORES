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
        return usuarioRepository.findAll()
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorNombreOrThrow(String nombre) {
        return usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + nombre));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarSinAdmins() {
        return usuarioRepository.findByRolNot(Rol.ADMIN)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO req) {
        if (id == null) {throw new IllegalArgumentException("id es requerido");}
        if (req == null) {throw new IllegalArgumentException("body es requerido");}

        Usuario usuario = obtenerPorIdOrThrow(id);

        if (req.getNombre() != null && !req.getNombre().isBlank()) {
            String nuevoNombre = req.getNombre().trim();
            if (!nuevoNombre.equals(usuario.getNombre()) && usuarioRepository.existsByNombre(nuevoNombre)) {
                throw new IllegalArgumentException("Ya existe el usuario: " + nuevoNombre);
            }
            usuario.setNombre(nuevoNombre);
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {usuario.setPasswordHash(passwordEncoder.encode(req.getPassword().trim()));}

        if (req.getRol() != null && !req.getRol().isBlank()) {usuario.setRol(Rol.valueOf(req.getRol().trim().toUpperCase()));}
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorIdOrThrow(id);
        if (usuario.getRol() == Rol.ADMIN) {throw new IllegalArgumentException("No se puede eliminar un usuario ADMIN");}
        usuarioRepository.delete(usuario);
    }

    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setRol(u.getRol().name());
        return dto;
    }
}
