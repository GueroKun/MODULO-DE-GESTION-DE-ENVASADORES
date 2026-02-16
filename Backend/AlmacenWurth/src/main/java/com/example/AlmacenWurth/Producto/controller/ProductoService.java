package com.example.AlmacenWurth.Producto.controller;

import com.example.AlmacenWurth.Producto.model.Producto;
import com.example.AlmacenWurth.Producto.model.ProductoDTO;
import com.example.AlmacenWurth.Producto.model.ProductoRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public ProductoDTO crear(ProductoDTO req) {
        if (req.getCodigo() == null || req.getCodigo().isBlank()) throw new IllegalArgumentException("codigo requerido");
        if (req.getNombre() == null || req.getNombre().isBlank()) throw new IllegalArgumentException("nombre requerido");
        if (req.getTotalUnidades() == null || req.getTotalUnidades() < 0) throw new IllegalArgumentException("totalUnidades >= 0");
        if (req.getStockActual() == null || req.getStockActual() < 0) throw new IllegalArgumentException("stockActual >= 0");
        if (req.getMinimoEnvasado() == null || req.getMinimoEnvasado() <= 0) throw new IllegalArgumentException("minimoEnvasado > 0");
        if (req.getUbicacionArticulo() == null || req.getUbicacionArticulo().isBlank()) throw new IllegalArgumentException("ubicacionArticulo requerido");
        if (req.getEstado() == null || req.getEstado().isBlank()) throw new IllegalArgumentException("estado requerido");
        if (req.getPrioridad() == null || req.getPrioridad().isBlank()) throw new IllegalArgumentException("prioridad requerida");

        String codigo = req.getCodigo().trim();
        if (productoRepository.existsByCodigo(codigo)) throw new IllegalArgumentException("Ya existe producto con codigo: " + codigo);

        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(req.getNombre().trim());
        p.setTotalUnidades(req.getTotalUnidades());
        p.setStockActual(req.getStockActual());
        p.setMinimoEnvasado(req.getMinimoEnvasado());
        p.setUbicacionArticulo(req.getUbicacionArticulo().trim());
        p.setEstado(Producto.Estado.valueOf(req.getEstado().trim()));
        p.setPrioridad(Producto.Prioridad.valueOf(req.getPrioridad().trim()));

        return toDTO(productoRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listar() {
        return productoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorCodigoOrThrow(String codigo) {
        return productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + codigo));
    }

    private ProductoDTO toDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setCodigo(p.getCodigo());
        dto.setNombre(p.getNombre());
        dto.setTotalUnidades(p.getTotalUnidades());
        dto.setStockActual(p.getStockActual());
        dto.setMinimoEnvasado(p.getMinimoEnvasado());
        dto.setUbicacionArticulo(p.getUbicacionArticulo());
        dto.setEstado(p.getEstado().name());
        dto.setPrioridad(p.getPrioridad().name());
        return dto;
    }
}

