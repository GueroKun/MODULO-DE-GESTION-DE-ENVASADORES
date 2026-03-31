package com.example.AlmacenWurth.Envasado.controller;

import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasado;
import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasadoDTO;
import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasadoRepository;
import com.example.AlmacenWurth.Envasador.controller.EnvasadorService;
import com.example.AlmacenWurth.Envasador.model.Envasador;
import com.example.AlmacenWurth.Producto.controller.ProductoService;
import com.example.AlmacenWurth.Producto.model.Producto;
import com.example.AlmacenWurth.Producto.model.ProductoRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProcesoEnvasadoService {

    private final ProcesoEnvasadoRepository repo;
    private final EnvasadorService envasadorService;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public ProcesoEnvasadoService(ProcesoEnvasadoRepository repo,
                                  EnvasadorService envasadorService,
                                  ProductoService productoService, ProductoRepository productoRepository) {
        this.repo = repo;
        this.envasadorService = envasadorService;
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    // Iniciar proceso
    @Transactional
    public ProcesoEnvasadoDTO iniciar(Long envasadorId, String codigoProducto) {
        if (envasadorId == null) throw new IllegalArgumentException("envasadorId requerido");
        if (codigoProducto == null || codigoProducto.isBlank()) throw new IllegalArgumentException("codigoProducto requerido");

        Envasador envasador = envasadorService.obtenerOrThrow(envasadorId);

        Producto producto = productoRepository.findByCodigoForUpdate(codigoProducto.trim())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + codigoProducto));

        if (producto.getEstado() != Producto.Estado.PENDIENTE) {
            throw new IllegalArgumentException("Solo se puede iniciar si el producto está PENDIENTE. Estado actual: " + producto.getEstado());
        }

        if (repo.existsByProductoIdAndHoraFinIsNull(producto.getId())) {
            throw new IllegalArgumentException("Ya existe un proceso activo para este producto.");
        }

        // (Opcional) evitar que el envasador tenga 2 activos
        // if (repo.existsByEnvasadorIdAndHoraFinIsNull(envasadorId)) {
        //     throw new IllegalArgumentException("El envasador ya tiene un proceso activo.");
        // }

        producto.setEstado(Producto.Estado.EN_PROCESO);
        productoRepository.save(producto);

        ProcesoEnvasado p = new ProcesoEnvasado();
        p.setEnvasador(envasador);
        p.setProducto(producto);

        p.setCodigoProducto(producto.getCodigo());
        p.setNombreProducto(producto.getNombre());
        p.setMinimoEnvasado(producto.getMinimoEnvasado());
        p.setHoraInicio(LocalDateTime.now());

        return toDTO(repo.save(p));
    }


    // Finalizar proceso
    @Transactional
    public ProcesoEnvasadoDTO finalizar(Long procesoId) {
        ProcesoEnvasado p = repo.findById(procesoId)
                .orElseThrow(() -> new NotFoundException("Proceso no encontrado: " + procesoId));

        if (p.getHoraFin() != null) {
            throw new IllegalArgumentException("El proceso ya está finalizado");
        }

        Producto producto = p.getProducto();

        Integer totalUnidadesActual = producto.getTotalUnidades();
        if (totalUnidadesActual == null) {
            totalUnidadesActual = 0;
        }

        if (totalUnidadesActual <= 0) {
            throw new IllegalArgumentException("El producto no tiene unidades pendientes por envasar");
        }

        Integer stockActual = producto.getStockActual();
        if (stockActual == null) {
            stockActual = 0;
        }


        int cantidadEnvasada = totalUnidadesActual;

        producto.setStockActual(stockActual + cantidadEnvasada);
        producto.setTotalUnidades(0);
        producto.setEstado(Producto.Estado.FINALIZADO);

        // Guardar cuánto se envasó en este proceso
        p.setCantidadEnvasada(cantidadEnvasada);
        p.setHoraFin(LocalDateTime.now());

        productoRepository.save(producto);
        return toDTO(repo.save(p));
    }

    @Transactional(readOnly = true)
    public List<ProcesoEnvasadoDTO> enProceso() {
        return repo.findByHoraFinIsNullOrderByHoraInicioAsc().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProcesoEnvasadoDTO> finalizados() {
        return repo.findByHoraFinIsNotNullOrderByHoraFinDesc()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProcesoEnvasadoDTO obtener(Long id) {
        ProcesoEnvasado p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Proceso no encontrado: " + id));
        return toDTO(p);
    }

    private ProcesoEnvasadoDTO toDTO(ProcesoEnvasado p) {
        ProcesoEnvasadoDTO dto = new ProcesoEnvasadoDTO();
        dto.setId(p.getId());
        dto.setEnvasadorId(p.getEnvasador().getId());
        dto.setEnvasadorNombre(p.getEnvasador().getNombre());
        dto.setCodigoProducto(p.getCodigoProducto());
        dto.setNombreProducto(p.getNombreProducto());
        dto.setMinimoEnvasado(p.getMinimoEnvasado());
        dto.setHoraInicio(p.getHoraInicio());
        dto.setHoraFin(p.getHoraFin());
        dto.setTiempoTranscurridoSegundos(p.getTiempoTranscurridoSegundos());
        dto.setCantidadEnvasada(p.getCantidadEnvasada());
        return dto;
    }
}
