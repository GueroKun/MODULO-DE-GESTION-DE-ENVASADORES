package com.example.AlmacenWurth.StockCaja.model;

import com.example.AlmacenWurth.Caja.model.Caja;
import jakarta.persistence.*;

@Entity
@Table(name = "stock_caja", uniqueConstraints = {
        @UniqueConstraint(name = "uk_stock_caja_ubicacion", columnNames = {"caja_id", "ubicacion"})
})
public class StockCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_id", nullable = false)
    private Caja caja;

    @Column(nullable = false, length = 120)
    private String ubicacion;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    public StockCaja() {
    }

    public Long getId() {
        return id;
    }

    public Caja getCaja() {
        return caja;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }
}