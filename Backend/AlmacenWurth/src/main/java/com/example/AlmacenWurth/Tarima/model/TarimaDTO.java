package com.example.AlmacenWurth.Tarima.model;

public class TarimaDTO {

    private Long id;
    private String numeroReferencia;
    private Long rackId;
    private String rackUbicacion;

    public TarimaDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public Long getRackId() {
        return rackId;
    }

    public String getRackUbicacion() {
        return rackUbicacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public void setRackId(Long rackId) {
        this.rackId = rackId;
    }

    public void setRackUbicacion(String rackUbicacion) {
        this.rackUbicacion = rackUbicacion;
    }
}