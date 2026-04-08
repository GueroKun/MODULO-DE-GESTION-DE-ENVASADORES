package com.example.AlmacenWurth.MovimientoCaja.model;

import java.time.LocalDateTime;

public class MovimientoCajaDTO {

    private Long id;
    private Long cajaId;
    private String codigoCaja;
    private String tipoMovimiento;
    private Integer cantidad;
    private String ubicacion;
    private String usuarioNombre;
    private LocalDateTime fechaMovimiento;
    private String observaciones;

    public MovimientoCajaDTO() {
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

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public String getObservaciones() {
        return observaciones;
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

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}