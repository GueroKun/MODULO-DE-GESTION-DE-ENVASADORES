package com.example.AlmacenWurth.Envasado.model;

public class IniciarProcesoRequestDTO {

    private Long envasadorId;
    private String codigoProducto;
    private Integer cantidadAsignada;
    private Integer minimoEnvasado;

    public IniciarProcesoRequestDTO() {}

    public Long getEnvasadorId() {
        return envasadorId;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public Integer getCantidadAsignada() {
        return cantidadAsignada;
    }

    public Integer getMinimoEnvasado() {
        return minimoEnvasado;
    }

    public void setEnvasadorId(Long envasadorId) {
        this.envasadorId = envasadorId;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setCantidadAsignada(Integer cantidadAsignada) {
        this.cantidadAsignada = cantidadAsignada;
    }

    public void setMinimoEnvasado(Integer minimoEnvasado) {
        this.minimoEnvasado = minimoEnvasado;
    }
}