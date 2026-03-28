package com.example.AlmacenWurth.ArticuloTarima.model;

import com.example.AlmacenWurth.Tarima.model.Tarima;
import jakarta.persistence.*;

@Entity
@Table(name = "articulo_tarima")
public class ArticuloTarima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarima_id", nullable = false)
    private Tarima tarima;

    @Column(nullable = false, length = 80)
    private String codigo;

    @Column(nullable = false)
    private Integer cantidad;

    public ArticuloTarima() {
    }

    public Long getId() {
        return id;
    }

    public Tarima getTarima() {
        return tarima;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTarima(Tarima tarima) {
        this.tarima = tarima;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}