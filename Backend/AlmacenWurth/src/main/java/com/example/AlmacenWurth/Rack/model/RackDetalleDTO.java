package com.example.AlmacenWurth.Rack.model;

import java.util.List;

public class RackDetalleDTO {

    private Long id;
    private String ubicacion;
    private Integer limiteTarimas;
    private Long totalTarimas;
    private List<TarimaRackDetalleDTO> tarimas;

    public RackDetalleDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getLimiteTarimas() {
        return limiteTarimas;
    }

    public Long getTotalTarimas() {
        return totalTarimas;
    }

    public List<TarimaRackDetalleDTO> getTarimas() {
        return tarimas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setLimiteTarimas(Integer limiteTarimas) {
        this.limiteTarimas = limiteTarimas;
    }

    public void setTotalTarimas(Long totalTarimas) {
        this.totalTarimas = totalTarimas;
    }

    public void setTarimas(List<TarimaRackDetalleDTO> tarimas) {
        this.tarimas = tarimas;
    }
}