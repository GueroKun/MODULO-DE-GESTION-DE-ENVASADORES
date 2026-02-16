package com.example.AlmacenWurth.Envasado.controller;

import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasado;
import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasadoDTO;
import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasadoRepository;
import com.example.AlmacenWurth.Envasador.controller.EnvasadorService;
import com.example.AlmacenWurth.Envasador.model.Envasador;
import com.example.AlmacenWurth.Producto.controller.ProductoService;
import com.example.AlmacenWurth.Producto.model.Producto;
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

    public ProcesoEnvasadoService(ProcesoEnvasadoRepository repo,
                                  EnvasadorService envasadorService,
                                  ProductoService productoService) {
        this.repo = repo;
        this.envasadorService = envasadorService;
        this.productoService = productoService;
    }

    // Iniciar proceso
    @Transactional
    public ProcesoEnvasadoDTO iniciar(Long envasadorId, String codigoProducto) {
        if (envasadorId == null) throw new IllegalArgumentException("envasadorId requerido");
        if (codigoProducto == null || codigoProducto.isBlank()) throw new IllegalArgumentException("codigoProducto requerido");

        Envasador envasador = envasadorService.obtenerOrThrow(envasadorId);
        Producto producto = productoService.obtenerPorCodigoOrThrow(codigoProducto.trim());

        // Cambiar estado del producto a EN_PROCESO (opcional pero lógico)
        producto.setEstado(Producto.Estado.EN_PROCESO);

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

        if (p.getHoraFin() != null) throw new IllegalArgumentException("El proceso ya está finalizado");

        p.setHoraFin(LocalDateTime.now());

        // Producto a FINALIZADO (si ese es el flujo)
        p.getProducto().setEstado(Producto.Estado.FINALIZADO);

        return toDTO(repo.save(p));
    }

    @Transactional(readOnly = true)
    public List<ProcesoEnvasadoDTO> enProceso() {
        return repo.findByHoraFinIsNullOrderByHoraInicioAsc().stream().map(this::toDTO).toList();
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
        return dto;
    }
}
