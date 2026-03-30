package com.example.AlmacenWurth.CajaCarton.model;

public class CajaCartonDTO {

    private Long id;
    private String codigo;
    private String descripcion;
    private String medida;
    private Integer stockActual;
    private Integer stockMinimo;
    private Boolean activo;
    private Boolean stockBajo;

    public CajaCartonDTO() {
    }

    public CajaCartonDTO(Long id, String codigo, String descripcion, String medida,
                         Integer stockActual, Integer stockMinimo, Boolean activo, Boolean stockBajo) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.medida = medida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
        this.stockBajo = stockBajo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getStockBajo() {
        return stockBajo;
    }

    public void setStockBajo(Boolean stockBajo) {
        this.stockBajo = stockBajo;
    }
}