package com.example.AlmacenWurth.Embarque.model;

import java.util.List;

public class EmbarquePreviewDTO {

    private String previewId;
    private String nombreArchivoOriginal;
    private Integer numeroHojas;
    private Integer totalFilasArchivo;
    private Integer totalColumnasArchivo;
    private Integer totalCeldasConDatos;
    private Integer totalRegistrosValidos;
    private Integer totalRegistrosDescartados;
    private List<EmbarqueDetallePreviewDTO> registros;

    public EmbarquePreviewDTO() {
    }

    public String getPreviewId() {
        return previewId;
    }

    public String getNombreArchivoOriginal() {
        return nombreArchivoOriginal;
    }

    public Integer getNumeroHojas() {
        return numeroHojas;
    }

    public Integer getTotalFilasArchivo() {
        return totalFilasArchivo;
    }

    public Integer getTotalColumnasArchivo() {
        return totalColumnasArchivo;
    }

    public Integer getTotalCeldasConDatos() {
        return totalCeldasConDatos;
    }

    public Integer getTotalRegistrosValidos() {
        return totalRegistrosValidos;
    }

    public Integer getTotalRegistrosDescartados() {
        return totalRegistrosDescartados;
    }

    public List<EmbarqueDetallePreviewDTO> getRegistros() {
        return registros;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }

    public void setNombreArchivoOriginal(String nombreArchivoOriginal) {
        this.nombreArchivoOriginal = nombreArchivoOriginal;
    }

    public void setNumeroHojas(Integer numeroHojas) {
        this.numeroHojas = numeroHojas;
    }

    public void setTotalFilasArchivo(Integer totalFilasArchivo) {
        this.totalFilasArchivo = totalFilasArchivo;
    }

    public void setTotalColumnasArchivo(Integer totalColumnasArchivo) {
        this.totalColumnasArchivo = totalColumnasArchivo;
    }

    public void setTotalCeldasConDatos(Integer totalCeldasConDatos) {
        this.totalCeldasConDatos = totalCeldasConDatos;
    }

    public void setTotalRegistrosValidos(Integer totalRegistrosValidos) {
        this.totalRegistrosValidos = totalRegistrosValidos;
    }

    public void setTotalRegistrosDescartados(Integer totalRegistrosDescartados) {
        this.totalRegistrosDescartados = totalRegistrosDescartados;
    }

    public void setRegistros(List<EmbarqueDetallePreviewDTO> registros) {
        this.registros = registros;
    }
}