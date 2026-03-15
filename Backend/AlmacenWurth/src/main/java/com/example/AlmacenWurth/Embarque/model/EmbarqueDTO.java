package com.example.AlmacenWurth.Embarque.model;

import java.time.LocalDateTime;

public class EmbarqueDTO {

    private Long id;
    private String nombreArchivoOriginal;
    private String nombreArchivoGuardado;
    private String rutaArchivo;
    private LocalDateTime fechaCarga;
    private String estatus;
    private Long tamanoBytes;
    private String tipoContenido;
    private Long usuarioId;
    private String usuarioNombre;
    private Integer numeroHojas;
    private Integer totalFilas;
    private Integer totalColumnas;
    private Integer totalCeldasConDatos;

    public EmbarqueDTO() {
    }

    public Long getId() {
        return id;
    }

    public Integer getNumeroHojas() {
        return numeroHojas;
    }

    public void setNumeroHojas(Integer numeroHojas) {
        this.numeroHojas = numeroHojas;
    }

    public Integer getTotalFilas() {
        return totalFilas;
    }

    public void setTotalFilas(Integer totalFilas) {
        this.totalFilas = totalFilas;
    }

    public Integer getTotalColumnas() {
        return totalColumnas;
    }

    public void setTotalColumnas(Integer totalColumnas) {
        this.totalColumnas = totalColumnas;
    }

    public Integer getTotalCeldasConDatos() {
        return totalCeldasConDatos;
    }

    public void setTotalCeldasConDatos(Integer totalCeldasConDatos) {
        this.totalCeldasConDatos = totalCeldasConDatos;
    }

    public String getNombreArchivoOriginal() {
        return nombreArchivoOriginal;
    }

    public String getNombreArchivoGuardado() {
        return nombreArchivoGuardado;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public String getEstatus() {
        return estatus;
    }

    public Long getTamanoBytes() {
        return tamanoBytes;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreArchivoOriginal(String nombreArchivoOriginal) {
        this.nombreArchivoOriginal = nombreArchivoOriginal;
    }

    public void setNombreArchivoGuardado(String nombreArchivoGuardado) {
        this.nombreArchivoGuardado = nombreArchivoGuardado;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setTamanoBytes(Long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
}