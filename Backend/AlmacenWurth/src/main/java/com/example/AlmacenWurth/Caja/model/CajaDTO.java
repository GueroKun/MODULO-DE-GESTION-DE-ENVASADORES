package com.example.AlmacenWurth.Caja.model;

public class CajaDTO {

    private Long id;
    private String codigo;
    private Boolean activo;

    public CajaDTO() {
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