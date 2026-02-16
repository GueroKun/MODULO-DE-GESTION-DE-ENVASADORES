package com.example.AlmacenWurth.Envasado.model;

import java.time.LocalDateTime;

public class ProcesoEnvasadoDTO {
    private Long id;

    private Long envasadorId;
    private String envasadorNombre;

    private String codigoProducto;
    private String nombreProducto;
    private Integer minimoEnvasado;

    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Long tiempoTranscurridoSegundos;

    public ProcesoEnvasadoDTO() {}

    public Long getId() { return id; }
    public Long getEnvasadorId() { return envasadorId; }
    public String getEnvasadorNombre() { return envasadorNombre; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public Integer getMinimoEnvasado() { return minimoEnvasado; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public LocalDateTime getHoraFin() { return horaFin; }
    public Long getTiempoTranscurridoSegundos() { return tiempoTranscurridoSegundos; }

    public void setId(Long id) { this.id = id; }
    public void setEnvasadorId(Long envasadorId) { this.envasadorId = envasadorId; }
    public void setEnvasadorNombre(String envasadorNombre) { this.envasadorNombre = envasadorNombre; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public void setMinimoEnvasado(Integer minimoEnvasado) { this.minimoEnvasado = minimoEnvasado; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }
    public void setTiempoTranscurridoSegundos(Long tiempoTranscurridoSegundos) { this.tiempoTranscurridoSegundos = tiempoTranscurridoSegundos; }
}

