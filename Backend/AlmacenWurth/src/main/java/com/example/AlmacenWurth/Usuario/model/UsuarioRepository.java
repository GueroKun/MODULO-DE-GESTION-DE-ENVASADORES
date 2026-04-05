package com.example.AlmacenWurth.Usuario.model;

import com.example.AlmacenWurth.Rol.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Usuario> findByRolNot(Rol rol);
}