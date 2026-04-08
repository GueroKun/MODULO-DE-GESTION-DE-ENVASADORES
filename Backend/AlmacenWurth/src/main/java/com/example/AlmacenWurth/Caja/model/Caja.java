package com.example.AlmacenWurth.Caja.model;

import jakarta.persistence.*;

@Entity
@Table(name = "caja", indexes = {
        @Index(name = "ux_caja_codigo", columnList = "codigo", unique = true)
})
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    @Column(nullable = false)
    private Boolean activo;

    public Caja() {
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}