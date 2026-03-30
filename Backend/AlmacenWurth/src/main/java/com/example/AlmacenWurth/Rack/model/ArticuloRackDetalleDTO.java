package com.example.AlmacenWurth.Rack.model;

public class ArticuloRackDetalleDTO {

    private Long id;
    private String codigo;
    private Integer cantidad;

    public ArticuloRackDetalleDTO() {
    }

    public Long getId() {
        return id;
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

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}