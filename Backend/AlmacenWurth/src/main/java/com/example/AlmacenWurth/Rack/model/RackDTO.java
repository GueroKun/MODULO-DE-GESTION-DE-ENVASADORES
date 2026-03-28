package com.example.AlmacenWurth.Rack.model;

public class RackDTO {

    private Long id;
    private String ubicacion;
    private Integer limiteTarimas;
    private Long totalTarimas;

    public RackDTO() {
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
}
