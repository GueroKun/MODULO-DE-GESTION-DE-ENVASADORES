package com.example.AlmacenWurth.Usuario.model;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String rol;

    public UsuarioDTO() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRol(String rol) { this.rol = rol; }
}
