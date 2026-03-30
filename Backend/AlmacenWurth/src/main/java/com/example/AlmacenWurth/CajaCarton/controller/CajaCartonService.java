package com.example.AlmacenWurth.CajaCarton.controller;

import com.example.AlmacenWurth.CajaCarton.model.*;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CajaCartonService {

    private final CajaCartonRepository cajaCartonRepository;
    private final MovimientoCajaCartonRepository movimientoRepository;

    public CajaCartonService(CajaCartonRepository cajaCartonRepository,
                             MovimientoCajaCartonRepository movimientoRepository) {
        this.cajaCartonRepository = cajaCartonRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public List<CajaCartonDTO> obtenerTodas() {
        return cajaCartonRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<CajaCartonDTO> obtenerActivas() {
        return cajaCartonRepository.findByActivoTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CajaCartonDTO obtenerPorId(Long id) {
        CajaCarton caja = cajaCartonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Caja de cartón no encontrada con id: " + id));
        return toDTO(caja);
    }

    @Transactional
    public CajaCartonDTO crear(CajaCartonDTO dto) {
        if (cajaCartonRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una caja con el código: " + dto.getCodigo());
        }

        CajaCarton caja = new CajaCarton();
        caja.setCodigo(dto.getCodigo());
        caja.setDescripcion(dto.getDescripcion());
        caja.setMedida(dto.getMedida());
        caja.setStockActual(dto.getStockActual() != null ? dto.getStockActual() : 0);
        caja.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0);
        caja.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        CajaCarton guardada = cajaCartonRepository.save(caja);

        if (guardada.getStockActual() > 0) {
            registrarMovimientoInterno(
                    guardada,
                    "ENTRADA",
                    guardada.getStockActual(),
                    "Stock inicial",
                    "ALTA_CAJA",
                    0,
                    guardada.getStockActual()
            );
        }

        return toDTO(guardada);
    }

    @Transactional
    public CajaCartonDTO actualizar(Long id, CajaCartonDTO dto) {
        CajaCarton caja = cajaCartonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Caja de cartón no encontrada con id: " + id));

        if (!caja.getCodigo().equals(dto.getCodigo()) && cajaCartonRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una caja con el código: " + dto.getCodigo());
        }

        caja.setCodigo(dto.getCodigo());
        caja.setDescripcion(dto.getDescripcion());
        caja.setMedida(dto.getMedida());
        caja.setStockMinimo(dto.getStockMinimo());
        if (dto.getActivo() != null) {
            caja.setActivo(dto.getActivo());
        }

        return toDTO(cajaCartonRepository.save(caja));
    }

    @Transactional
    public CajaCartonDTO desactivar(Long id) {
        CajaCarton caja = cajaCartonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Caja de cartón no encontrada con id: " + id));

        caja.setActivo(false);
        return toDTO(cajaCartonRepository.save(caja));
    }

    @Transactional
    public MovimientoCajaCartonDTO registrarEntrada(Long cajaId, Integer cantidad, String motivo, String referencia) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a 0");
        }

        CajaCarton caja = cajaCartonRepository.findById(cajaId)
                .orElseThrow(() -> new NotFoundException("Caja de cartón no encontrada con id: " + cajaId));

        Integer stockAnterior = caja.getStockActual();
        Integer stockNuevo = stockAnterior + cantidad;

        caja.setStockActual(stockNuevo);
        cajaCartonRepository.save(caja);

        MovimientoCajaCarton movimiento = registrarMovimientoInterno(
                caja,
                "ENTRADA",
                cantidad,
                motivo,
                referencia,
                stockAnterior,
                stockNuevo
        );

        return toMovimientoDTO(movimiento);
    }

    @Transactional
    public MovimientoCajaCartonDTO registrarSalida(Long cajaId, Integer cantidad, String motivo, String referencia) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de salida debe ser mayor a 0");
        }

        CajaCarton caja = cajaCartonRepository.findById(cajaId)
                .orElseThrow(() -> new NotFoundException("Caja de cartón no encontrada con id: " + cajaId));

        Integer stockAnterior = caja.getStockActual();

        if (stockAnterior < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + stockAnterior);
        }

        Integer stockNuevo = stockAnterior - cantidad;

        caja.setStockActual(stockNuevo);
        cajaCartonRepository.save(caja);

        MovimientoCajaCarton movimiento = registrarMovimientoInterno(
                caja,
                "SALIDA",
                cantidad,
                motivo,
                referencia,
                stockAnterior,
                stockNuevo
        );

        return toMovimientoDTO(movimiento);
    }

    public List<MovimientoCajaCartonDTO> obtenerMovimientos() {
        return movimientoRepository.findAllByOrderByFechaMovimientoDesc()
                .stream()
                .map(this::toMovimientoDTO)
                .toList();
    }

    public List<MovimientoCajaCartonDTO> obtenerMovimientosPorCaja(Long cajaId) {
        if (!cajaCartonRepository.existsById(cajaId)) {
            throw new NotFoundException("Caja de cartón no encontrada con id: " + cajaId);
        }

        return movimientoRepository.findByCajaCartonIdOrderByFechaMovimientoDesc(cajaId)
                .stream()
                .map(this::toMovimientoDTO)
                .toList();
    }

    public List<CajaCartonDTO> obtenerStockBajo() {
        return cajaCartonRepository.findByActivoTrue()
                .stream()
                .filter(c -> c.getStockActual() <= c.getStockMinimo())
                .map(this::toDTO)
                .toList();
    }

    private MovimientoCajaCarton registrarMovimientoInterno(CajaCarton caja,
                                                            String tipoMovimiento,
                                                            Integer cantidad,
                                                            String motivo,
                                                            String referencia,
                                                            Integer stockAnterior,
                                                            Integer stockNuevo) {
        MovimientoCajaCarton movimiento = new MovimientoCajaCarton();
        movimiento.setCajaCarton(caja);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);
        movimiento.setReferencia(referencia);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockResultante(stockNuevo);

        return movimientoRepository.save(movimiento);
    }

    private CajaCartonDTO toDTO(CajaCarton caja) {
        Boolean stockBajo = caja.getStockActual() <= caja.getStockMinimo();

        return new CajaCartonDTO(
                caja.getId(),
                caja.getCodigo(),
                caja.getDescripcion(),
                caja.getMedida(),
                caja.getStockActual(),
                caja.getStockMinimo(),
                caja.getActivo(),
                stockBajo
        );
    }

    private MovimientoCajaCartonDTO toMovimientoDTO(MovimientoCajaCarton mov) {
        return new MovimientoCajaCartonDTO(
                mov.getId(),
                mov.getCajaCarton().getId(),
                mov.getCajaCarton().getCodigo(),
                mov.getCajaCarton().getDescripcion(),
                mov.getTipoMovimiento(),
                mov.getCantidad(),
                mov.getMotivo(),
                mov.getReferencia(),
                mov.getFechaMovimiento(),
                mov.getStockAnterior(),
                mov.getStockResultante()
        );
    }
}