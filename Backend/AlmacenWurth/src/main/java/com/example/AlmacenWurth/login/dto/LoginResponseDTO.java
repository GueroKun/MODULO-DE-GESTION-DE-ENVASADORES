package com.example.AlmacenWurth.login.dto;

public class LoginResponseDTO {
    private String token;
    private String nombre;
    private String rol;

    public LoginResponseDTO() {}

    public String getToken() { return token; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }

    public void setToken(String token) { this.token = token; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRol(String rol) { this.rol = rol; }
}
