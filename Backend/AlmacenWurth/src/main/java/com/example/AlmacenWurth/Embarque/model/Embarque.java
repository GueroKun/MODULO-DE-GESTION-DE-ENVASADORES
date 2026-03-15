package com.example.AlmacenWurth.Embarque.model;

import com.example.AlmacenWurth.Usuario.model.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "embarque")
public class Embarque {

    public enum Estatus {
        PENDIENTE_PROCESAR,
        PROCESADO,
        ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_archivo_original", nullable = false, length = 255)
    private String nombreArchivoOriginal;

    @Column(name = "nombre_archivo_guardado", nullable = false, length = 255, unique = true)
    private String nombreArchivoGuardado;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "fecha_carga", nullable = false)
    private LocalDateTime fechaCarga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Estatus estatus;

    @Column(name = "tamano_bytes", nullable = false)
    private Long tamanoBytes;

    @Column(name = "tipo_contenido", length = 120)
    private String tipoContenido;

    @Column(name = "numero_hojas")
    private Integer numeroHojas;

    @Column(name = "total_filas")
    private Integer totalFilas;

    @Column(name = "total_columnas")
    private Integer totalColumnas;

    @Column(name = "total_celdas_con_datos")
    private Integer totalCeldasConDatos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Embarque() {
    }

    public Long getId() {
        return id;
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

    public Estatus getEstatus() {
        return estatus;
    }

    public Long getTamanoBytes() {
        return tamanoBytes;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public Usuario getUsuario() {
        return usuario;
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

    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }

    public void setTamanoBytes(Long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
}