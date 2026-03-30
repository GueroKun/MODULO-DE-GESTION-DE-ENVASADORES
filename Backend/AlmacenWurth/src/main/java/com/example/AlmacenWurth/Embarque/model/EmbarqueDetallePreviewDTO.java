package com.example.AlmacenWurth.Embarque.model;

public class EmbarqueDetallePreviewDTO {

    private String codigo;
    private String descripcion;
    private Integer cantidad;
    private String abc;

    public EmbarqueDetallePreviewDTO() {
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