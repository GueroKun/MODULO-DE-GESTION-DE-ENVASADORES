package com.example.AlmacenWurth.StockCaja.model;

public class StockCajaDTO {

    private Long id;
    private Long cajaId;
    private String codigoCaja;
    private String ubicacion;
    private Integer stockActual;
    private Boolean cajaActiva;

    public StockCajaDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getCajaId() {
        return cajaId;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public Boolean getCajaActiva() {
        return cajaActiva;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCajaId(Long cajaId) {
        this.cajaId = cajaId;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public void setCajaActiva(Boolean cajaActiva) {
        this.cajaActiva = cajaActiva;
    }
}