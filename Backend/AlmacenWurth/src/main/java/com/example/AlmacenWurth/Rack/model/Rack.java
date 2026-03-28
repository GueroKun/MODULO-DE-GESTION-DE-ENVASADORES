package com.example.AlmacenWurth.Rack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rack")
public class Rack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String ubicacion;

    @Column(name = "limite_tarimas", nullable = false)
    private Integer limiteTarimas;

    public Rack() {
    }

    public Long getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getLimiteTarimas() {
        return limiteTarimas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setLimiteTarimas(Integer limiteTarimas) {
        this.limiteTarimas = limiteTarimas;
    }
}