package com.example.AlmacenWurth.Envasador.model;

import jakarta.persistence.*;

@Entity
@Table(name = "envasador")
public class Envasador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String nombre;

    public Envasador() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

