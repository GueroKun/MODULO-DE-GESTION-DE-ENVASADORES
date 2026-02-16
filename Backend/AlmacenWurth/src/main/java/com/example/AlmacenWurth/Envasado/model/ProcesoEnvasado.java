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

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="envasador_id", nullable=false)
    private Envasador envasador;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="producto_id", nullable=false)
    private Producto producto;

    // Snapshot por si el producto cambia después
    @Column(name="codigo_producto", nullable=false, length=50)
    private String codigoProducto;

    @Column(name="nombre_producto", nullable=false, length=200)
    private String nombreProducto;

    @Column(name="minimo_envasado", nullable=false)
    private Integer minimoEnvasado;

    @Column(name="hora_inicio", nullable=false)
    private LocalDateTime horaInicio;

    @Column(name="hora_fin")
    private LocalDateTime horaFin;

    public ProcesoEnvasado() {}

    public Long getId() { return id; }
    public Envasador getEnvasador() { return envasador; }
    public Producto getProducto() { return producto; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public Integer getMinimoEnvasado() { return minimoEnvasado; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public LocalDateTime getHoraFin() { return horaFin; }

    public void setId(Long id) { this.id = id; }
    public void setEnvasador(Envasador envasador) { this.envasador = envasador; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public void setMinimoEnvasado(Integer minimoEnvasado) { this.minimoEnvasado = minimoEnvasado; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }

    @Transient
    public Long getTiempoTranscurridoSegundos() {
        if (horaInicio == null) return null;
        LocalDateTime fin = (horaFin != null) ? horaFin : LocalDateTime.now();
        return Duration.between(horaInicio, fin).getSeconds();
    }
}
