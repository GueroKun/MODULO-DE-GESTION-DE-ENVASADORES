package com.example.AlmacenWurth.CajaCarton.model;

import java.time.LocalDateTime;

public class MovimientoCajaCartonDTO {

    private Long id;
    private Long cajaCartonId;
    private String codigoCaja;
    private String descripcionCaja;
    private String tipoMovimiento;
    private Integer cantidad;
    private String motivo;
    private String referencia;
    private LocalDateTime fechaMovimiento;
    private Integer stockAnterior;
    private Integer stockResultante;

    public MovimientoCajaCartonDTO() {
    }

    public MovimientoCajaCartonDTO(Long id, Long cajaCartonId, String codigoCaja, String descripcionCaja,
                                   String tipoMovimiento, Integer cantidad, String motivo, String referencia,
                                   LocalDateTime fechaMovimiento, Integer stockAnterior, Integer stockResultante) {
        this.id = id;
        this.cajaCartonId = cajaCartonId;
        this.codigoCaja = codigoCaja;
        this.descripcionCaja = descripcionCaja;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.referencia = referencia;
        this.fechaMovimiento = fechaMovimiento;
        this.stockAnterior = stockAnterior;
        this.stockResultante = stockResultante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCajaCartonId() {
        return cajaCartonId;
    }

    public void setCajaCartonId(Long cajaCartonId) {
        this.cajaCartonId = cajaCartonId;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getDescripcionCaja() {
        return descripcionCaja;
    }

    public void setDescripcionCaja(String descripcionCaja) {
        this.descripcionCaja = descripcionCaja;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getStockResultante() {
        return stockResultante;
    }

    public void setStockResultante(Integer stockResultante) {
        this.stockResultante = stockResultante;
    }
}