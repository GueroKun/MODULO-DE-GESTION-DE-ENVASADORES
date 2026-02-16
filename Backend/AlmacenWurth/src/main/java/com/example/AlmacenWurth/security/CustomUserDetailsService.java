package com.example.AlmacenWurth.security;

import com.example.AlmacenWurth.Usuario.model.Usuario;
import com.example.AlmacenWurth.Usuario.model.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public CustomUserDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = repo.findByNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Spring Security espera ROLE_
        String role = "ROLE_" + u.getRol().name();
        return new org.springframework.security.core.userdetails.User(
                u.getNombre(),
                u.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
