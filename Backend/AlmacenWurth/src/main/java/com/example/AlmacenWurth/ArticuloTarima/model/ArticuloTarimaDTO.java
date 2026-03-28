package com.example.AlmacenWurth.ArticuloTarima.model;

public class ArticuloTarimaDTO {

    private Long id;
    private Long tarimaId;
    private String numeroReferenciaTarima;
    private String codigo;
    private Integer cantidad;

    public ArticuloTarimaDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getTarimaId() {
        return tarimaId;
    }

    public String getNumeroReferenciaTarima() {
        return numeroReferenciaTarima;
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

    public void setTarimaId(Long tarimaId) {
        this.tarimaId = tarimaId;
    }

    public void setNumeroReferenciaTarima(String numeroReferenciaTarima) {
        this.numeroReferenciaTarima = numeroReferenciaTarima;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}