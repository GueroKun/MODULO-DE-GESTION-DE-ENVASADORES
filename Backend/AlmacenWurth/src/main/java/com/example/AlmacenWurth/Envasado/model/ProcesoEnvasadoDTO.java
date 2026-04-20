package com.example.AlmacenWurth.Envasado.model;

import java.time.LocalDateTime;

public class ProcesoEnvasadoDTO {
    private Long id;

    private Long envasadorId;
    private String envasadorNombre;

    private String codigoProducto;
    private String nombreProducto;
    private Integer minimoEnvasado;
    private Integer cantidadAsignada;
    private Integer cantidadEnvasada;
    private Integer cantidadPaquetes;

    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Long tiempoTranscurridoSegundos;

    public ProcesoEnvasadoDTO() {}

    public Long getId() {
        return id;
    }

    public Long getEnvasadorId() {
        return envasadorId;
    }

    public String getEnvasadorNombre() {
        return envasadorNombre;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public Integer getMinimoEnvasado() {
        return minimoEnvasado;
    }

    public Integer getCantidadAsignada() {
        return cantidadAsignada;
    }

    public Integer getCantidadEnvasada() {
        return cantidadEnvasada;
    }

    public Integer getCantidadPaquetes() {
        return cantidadPaquetes;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public Long getTiempoTranscurridoSegundos() {
        return tiempoTranscurridoSegundos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnvasadorId(Long envasadorId) {
        this.envasadorId = envasadorId;
    }

    public void setEnvasadorNombre(String envasadorNombre) {
        this.envasadorNombre = envasadorNombre;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setMinimoEnvasado(Integer minimoEnvasado) {
        this.minimoEnvasado = minimoEnvasado;
    }

    public void setCantidadAsignada(Integer cantidadAsignada) {
        this.cantidadAsignada = cantidadAsignada;
    }

    public void setCantidadEnvasada(Integer cantidadEnvasada) {
        this.cantidadEnvasada = cantidadEnvasada;
    }

    public void setCantidadPaquetes(Integer cantidadPaquetes) {
        this.cantidadPaquetes = cantidadPaquetes;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setTiempoTranscurridoSegundos(Long tiempoTranscurridoSegundos) {
        this.tiempoTranscurridoSegundos = tiempoTranscurridoSegundos;
    }
}