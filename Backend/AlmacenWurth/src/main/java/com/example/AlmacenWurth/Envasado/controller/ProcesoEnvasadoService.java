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
                                  ProductoService productoService,
                                  ProductoRepository productoRepository) {
        this.repo = repo;
        this.envasadorService = envasadorService;
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public ProcesoEnvasadoDTO iniciar(Long envasadorId,
                                      String codigoProducto,
                                      Integer cantidadAsignada,
                                      Integer minimoEnvasado) {

        if (envasadorId == null) {
            throw new IllegalArgumentException("envasadorId requerido");
        }

        if (codigoProducto == null || codigoProducto.isBlank()) {
            throw new IllegalArgumentException("codigoProducto requerido");
        }

        if (cantidadAsignada == null || cantidadAsignada <= 0) {
            throw new IllegalArgumentException("cantidadAsignada requerida y mayor a 0");
        }

        if (minimoEnvasado == null || minimoEnvasado <= 0) {
            throw new IllegalArgumentException("minimoEnvasado requerido y mayor a 0");
        }

        if (cantidadAsignada % minimoEnvasado != 0) {
            throw new IllegalArgumentException("La cantidad asignada debe dividirse exactamente entre el mínimo de envasado");
        }

        Envasador envasador = envasadorService.obtenerOrThrow(envasadorId);

        Producto producto = productoRepository.findByCodigoForUpdate(codigoProducto.trim())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + codigoProducto));

        Integer totalUnidades = producto.getTotalUnidades();
        if (totalUnidades == null) {
            totalUnidades = 0;
        }

        if (totalUnidades <= 0) {
            throw new IllegalArgumentException("El producto no tiene unidades disponibles para asignar");
        }

        Integer cantidadYaAsignada = repo.sumarCantidadAsignadaActivaPorProducto(producto.getId());
        if (cantidadYaAsignada == null) {
            cantidadYaAsignada = 0;
        }

        int disponibleParaAsignar = totalUnidades - cantidadYaAsignada;

        if (disponibleParaAsignar <= 0) {
            throw new IllegalArgumentException("Ya no hay unidades disponibles para asignar en este producto");
        }

        if (cantidadAsignada > disponibleParaAsignar) {
            throw new IllegalArgumentException(
                    "La cantidad asignada supera lo disponible para este producto. Disponible: " + disponibleParaAsignar
            );
        }

        // Opcional: descomenta si quieres evitar que un envasador tenga dos procesos activos al mismo tiempo
        // if (repo.existsByEnvasadorIdAndHoraFinIsNull(envasadorId)) {
        //     throw new IllegalArgumentException("El envasador ya tiene un proceso activo.");
        // }

        ProcesoEnvasado p = new ProcesoEnvasado();
        p.setEnvasador(envasador);
        p.setProducto(producto);
        p.setNombreEnvasador(envasador.getNombre());
        p.setCodigoProducto(producto.getCodigo());
        p.setNombreProducto(producto.getNombre());
        p.setMinimoEnvasado(minimoEnvasado);
        p.setCantidadAsignada(cantidadAsignada);
        p.setCantidadPaquetes(cantidadAsignada / minimoEnvasado);
        p.setHoraInicio(LocalDateTime.now());

        if (producto.getEstado() == Producto.Estado.PENDIENTE) {
            producto.setEstado(Producto.Estado.EN_PROCESO);
            productoRepository.save(producto);
        }

        return toDTO(repo.save(p));
    }

    @Transactional
    public ProcesoEnvasadoDTO finalizar(Long procesoId) {
        ProcesoEnvasado p = repo.findById(procesoId)
                .orElseThrow(() -> new NotFoundException("Proceso no encontrado: " + procesoId));

        if (p.getHoraFin() != null) {
            throw new IllegalArgumentException("El proceso ya está finalizado");
        }

        Producto producto = productoRepository.findById(p.getProducto().getId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        int totalUnidadesActual = producto.getTotalUnidades() != null ? producto.getTotalUnidades() : 0;
        int stockActual = producto.getStockActual() != null ? producto.getStockActual() : 0;
        int cantidadEnvasada = p.getCantidadAsignada() != null ? p.getCantidadAsignada() : 0;

        if (cantidadEnvasada <= 0) {
            throw new IllegalArgumentException("El proceso no tiene una cantidad asignada válida");
        }

        if (totalUnidadesActual < cantidadEnvasada) {
            throw new IllegalArgumentException("El producto no tiene suficientes unidades para finalizar este proceso");
        }

        producto.setStockActual(stockActual + cantidadEnvasada);
        producto.setTotalUnidades(totalUnidadesActual - cantidadEnvasada);

        if (producto.getTotalUnidades() == 0) {
            producto.setEstado(Producto.Estado.FINALIZADO);
        } else {
            producto.setEstado(Producto.Estado.EN_PROCESO);
        }

        p.setCantidadEnvasada(cantidadEnvasada);
        p.setHoraFin(LocalDateTime.now());

        productoRepository.save(producto);
        return toDTO(repo.save(p));
    }

    @Transactional(readOnly = true)
    public List<ProcesoEnvasadoDTO> enProceso() {
        return repo.findByHoraFinIsNullOrderByHoraInicioAsc()
                .stream()
                .map(this::toDTO)
                .toList();
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

        if (p.getEnvasador() != null) {
            dto.setEnvasadorId(p.getEnvasador().getId());
            dto.setEnvasadorNombre(p.getEnvasador().getNombre());
        } else {
            dto.setEnvasadorNombre(p.getNombreEnvasador());
        }

        if (p.getProducto() != null) {
            dto.setCodigoProducto(p.getProducto().getCodigo());
            dto.setNombreProducto(p.getProducto().getNombre());
        } else {
            dto.setCodigoProducto(p.getCodigoProducto());
            dto.setNombreProducto(p.getNombreProducto());
        }

        dto.setMinimoEnvasado(p.getMinimoEnvasado());
        dto.setCantidadAsignada(p.getCantidadAsignada());
        dto.setCantidadEnvasada(p.getCantidadEnvasada());
        dto.setCantidadPaquetes(p.getCantidadPaquetes());
        dto.setHoraInicio(p.getHoraInicio());
        dto.setHoraFin(p.getHoraFin());
        dto.setTiempoTranscurridoSegundos(p.getTiempoTranscurridoSegundos());

        return dto;
    }
}