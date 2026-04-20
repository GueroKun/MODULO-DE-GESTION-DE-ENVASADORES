package com.example.AlmacenWurth.Envasado.model;

import com.example.AlmacenWurth.Envasador.model.Envasador;
import com.example.AlmacenWurth.Producto.model.Producto;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "proceso_envasado", indexes = {
        @Index(name="idx_proc_inicio", columnList="hora_inicio")
})
public class ProcesoEnvasado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "envasador_id")
    private Envasador envasador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // Snapshot por si el producto cambia después
    @Column(name = "codigo_producto", nullable = false, length = 50)
    private String codigoProducto;

    @Column(name = "nombre_producto", nullable = false, length = 200)
    private String nombreProducto;

    @Column(name = "nombre_envasador", nullable = false, length = 120)
    private String nombreEnvasador;

    @Column(name = "minimo_envasado", nullable = false)
    private Integer minimoEnvasado;

    @Column(name = "cantidad_asignada", nullable = false)
    private Integer cantidadAsignada;

    @Column(name = "cantidad_envasada")
    private Integer cantidadEnvasada;

    @Column(name = "cantidad_paquetes", nullable = false)
    private Integer cantidadPaquetes;

    @Column(name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;

    @Column(name = "hora_fin")
    private LocalDateTime horaFin;

    public ProcesoEnvasado() {}

    public Long getId() {
        return id;
    }

    public Envasador getEnvasador() {
        return envasador;
    }

    public Producto getProducto() {
        return producto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getNombreEnvasador() {
        return nombreEnvasador;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnvasador(Envasador envasador) {
        this.envasador = envasador;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setNombreEnvasador(String nombreEnvasador) {
        this.nombreEnvasador = nombreEnvasador;
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

    @Transient
    public Long getTiempoTranscurridoSegundos() {
        if (horaInicio == null) return null;
        LocalDateTime fin = (horaFin != null) ? horaFin : LocalDateTime.now();
        return Duration.between(horaInicio, fin).getSeconds();
    }
}