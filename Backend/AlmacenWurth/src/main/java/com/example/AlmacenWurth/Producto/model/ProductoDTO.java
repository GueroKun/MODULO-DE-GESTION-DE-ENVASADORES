package com.example.AlmacenWurth.Producto.model;

public class ProductoDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Integer totalUnidades;
    private Integer stockActual;
    private Integer minimoEnvasado;
    private String ubicacionArticulo;
    private String estado;
    private String prioridad;

    public ProductoDTO() {}

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public Integer getTotalUnidades() { return totalUnidades; }
    public Integer getStockActual() { return stockActual; }
    public Integer getMinimoEnvasado() { return minimoEnvasado; }
    public String getUbicacionArticulo() { return ubicacionArticulo; }
    public String getEstado() { return estado; }
    public String getPrioridad() { return prioridad; }

    public void setId(Long id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTotalUnidades(Integer totalUnidades) { this.totalUnidades = totalUnidades; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public void setMinimoEnvasado(Integer minimoEnvasado) { this.minimoEnvasado = minimoEnvasado; }
    public void setUbicacionArticulo(String ubicacionArticulo) { this.ubicacionArticulo = ubicacionArticulo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
}

