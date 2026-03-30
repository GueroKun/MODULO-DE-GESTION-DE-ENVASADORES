package com.example.AlmacenWurth.Rack.model;

import java.util.List;

public class TarimaRackDetalleDTO {

    private Long id;
    private String numeroReferencia;
    private List<ArticuloRackDetalleDTO> articulos;

    public TarimaRackDetalleDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public List<ArticuloRackDetalleDTO> getArticulos() {
        return articulos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public void setArticulos(List<ArticuloRackDetalleDTO> articulos) {
        this.articulos = articulos;
    }
}