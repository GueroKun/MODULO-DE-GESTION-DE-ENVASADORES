package com.example.AlmacenWurth.Envasado.model;

public class IniciarProcesoRequestDTO {
    private Long envasadorId;
    private String codigoProducto;

    public IniciarProcesoRequestDTO() {}

    public Long getEnvasadorId() { return envasadorId; }
    public String getCodigoProducto() { return codigoProducto; }

    public void setEnvasadorId(Long envasadorId) { this.envasadorId = envasadorId; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
}
