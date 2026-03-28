package com.example.AlmacenWurth.Tarima.model;

import com.example.AlmacenWurth.Rack.model.Rack;
import jakarta.persistence.*;

@Entity
@Table(name = "tarima")
public class Tarima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_referencia", nullable = false, length = 80, unique = true)
    private String numeroReferencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rack_id", nullable = false)
    private Rack rack;

    public Tarima() {
    }

    public Long getId() {
        return id;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public Rack getRack() {
        return rack;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }
}