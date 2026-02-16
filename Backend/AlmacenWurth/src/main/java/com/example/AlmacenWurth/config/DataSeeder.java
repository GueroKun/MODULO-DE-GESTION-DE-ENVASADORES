package com.example.AlmacenWurth.config;

import com.example.AlmacenWurth.Rol.model.Rol;
import com.example.AlmacenWurth.Usuario.model.Usuario;
import com.example.AlmacenWurth.Usuario.model.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminNombre = "admin";

        if (!usuarioRepository.existsByNombre(adminNombre)) {
            Usuario admin = new Usuario();
            admin.setNombre(adminNombre);
            admin.setPasswordHash(passwordEncoder.encode("1234"));
            admin.setRol(Rol.ADMIN);

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario admin creado por defecto (admin / 1234)");
        } else {
            System.out.println("ℹ️ Usuario admin ya existe, no se crea de nuevo.");
        }
    }
}
