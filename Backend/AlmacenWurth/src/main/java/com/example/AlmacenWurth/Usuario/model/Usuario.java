package com.example.AlmacenWurth.Usuario.model;

import com.example.AlmacenWurth.Rol.model.Rol;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario", indexes = {
        @Index(name="ux_usuario_nombre", columnList="nombre", unique = true)
})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=80, unique = true)
    private String nombre;

    @Column(nullable=false, length=200)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Rol rol;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
