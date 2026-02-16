package com.example.AlmacenWurth.login.dto;

public class LoginRequestDTO {
    private String nombre;
    private String password;

    public LoginRequestDTO() {}

    public String getNombre() { return nombre; }
    public String getPassword() { return password; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPassword(String password) { this.password = password; }
}
