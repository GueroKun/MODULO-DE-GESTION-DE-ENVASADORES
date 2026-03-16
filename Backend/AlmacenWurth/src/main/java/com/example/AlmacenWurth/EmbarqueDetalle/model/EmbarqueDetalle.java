package com.example.AlmacenWurth.EmbarqueDetalle.model;

import com.example.AlmacenWurth.Embarque.model.Embarque;
import jakarta.persistence.*;

@Entity
@Table(name = "embarque_detalle")
public class EmbarqueDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "embarque_id", nullable = false)
    private Embarque embarque;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 20)
    private String abc;

    public EmbarqueDetalle() {
    }

    public Long getId() {
        return id;
    }

    public Embarque getEmbarque() {
        return embarque;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getAbc() {
        return abc;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmbarque(Embarque embarque) {
        this.embarque = embarque;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }
}