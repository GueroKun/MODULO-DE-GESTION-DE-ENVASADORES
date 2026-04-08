package com.example.AlmacenWurth.MovimientoCaja.controller;

import com.example.AlmacenWurth.Caja.model.Caja;
import com.example.AlmacenWurth.Caja.model.CajaRepository;
import com.example.AlmacenWurth.MovimientoCaja.model.MovimientoCaja;
import com.example.AlmacenWurth.MovimientoCaja.model.MovimientoCajaDTO;
import com.example.AlmacenWurth.MovimientoCaja.model.MovimientoCajaRepository;
import com.example.AlmacenWurth.StockCaja.model.StockCaja;
import com.example.AlmacenWurth.StockCaja.model.StockCajaRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoCajaService {

    private final MovimientoCajaRepository movimientoCajaRepository;
    private final CajaRepository cajaRepository;
    private final StockCajaRepository stockCajaRepository;

    public MovimientoCajaService(MovimientoCajaRepository movimientoCajaRepository,
                                 CajaRepository cajaRepository,
                                 StockCajaRepository stockCajaRepository) {
        this.movimientoCajaRepository = movimientoCajaRepository;
        this.cajaRepository = cajaRepository;
        this.stockCajaRepository = stockCajaRepository;
    }

    @Transactional
    public MovimientoCajaDTO registrarEntrada(MovimientoCajaDTO req) {
        validarRequestMovimiento(req);

        Caja caja = cajaRepository.findById(req.getCajaId())
                .orElseThrow(() -> new NotFoundException("Caja no encontrada: " + req.getCajaId()));

        if (!Boolean.TRUE.equals(caja.getActivo())) {
            throw new IllegalArgumentException("La caja está inactiva");
        }

        StockCaja stockCaja = stockCajaRepository.findByCajaIdAndUbicacion(caja.getId(), req.getUbicacion().trim())
                .orElseGet(() -> {
                    StockCaja nuevo = new StockCaja();
                    nuevo.setCaja(caja);
                    nuevo.setUbicacion(req.getUbicacion().trim());
                    nuevo.setStockActual(0);
                    return nuevo;
                });

        stockCaja.setStockActual(stockCaja.getStockActual() + req.getCantidad());
        stockCajaRepository.save(stockCaja);

        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCaja(caja);
        movimiento.setTipoMovimiento(MovimientoCaja.TipoMovimiento.ENTRADA);
        movimiento.setCantidad(req.getCantidad());
        movimiento.setUbicacion(req.getUbicacion().trim());
        movimiento.setUsuarioNombre(req.getUsuarioNombre().trim());
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setObservaciones(req.getObservaciones());

        return toDTO(movimientoCajaRepository.save(movimiento));
    }

    @Transactional
    public MovimientoCajaDTO registrarSalida(MovimientoCajaDTO req) {
        validarRequestMovimiento(req);

        Caja caja = cajaRepository.findById(req.getCajaId())
                .orElseThrow(() -> new NotFoundException("Caja no encontrada: " + req.getCajaId()));

        StockCaja stockCaja = stockCajaRepository.findByCajaIdAndUbicacion(caja.getId(), req.getUbicacion().trim())
                .orElseThrow(() -> new IllegalArgumentException("No existe stock para esa caja en esa ubicación"));

        if (stockCaja.getStockActual() < req.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + stockCaja.getStockActual());
        }

        stockCaja.setStockActual(stockCaja.getStockActual() - req.getCantidad());
        stockCajaRepository.save(stockCaja);

        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCaja(caja);
        movimiento.setTipoMovimiento(MovimientoCaja.TipoMovimiento.SALIDA);
        movimiento.setCantidad(req.getCantidad());
        movimiento.setUbicacion(req.getUbicacion().trim());
        movimiento.setUsuarioNombre(req.getUsuarioNombre().trim());
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setObservaciones(req.getObservaciones());

        return toDTO(movimientoCajaRepository.save(movimiento));
    }

    @Transactional(readOnly = true)
    public List<MovimientoCajaDTO> listarEntradas() {
        return movimientoCajaRepository.findByTipoMovimientoOrderByFechaMovimientoDesc(MovimientoCaja.TipoMovimiento.ENTRADA)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoCajaDTO> listarSalidas() {
        return movimientoCajaRepository.findByTipoMovimientoOrderByFechaMovimientoDesc(MovimientoCaja.TipoMovimiento.SALIDA)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoCajaDTO> historialPorCaja(Long cajaId) {
        return movimientoCajaRepository.findByCajaIdOrderByFechaMovimientoDesc(cajaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void validarRequestMovimiento(MovimientoCajaDTO req) {
        if (req.getCajaId() == null) {
            throw new IllegalArgumentException("cajaId requerido");
        }
        if (req.getCantidad() == null || req.getCantidad() <= 0) {
            throw new IllegalArgumentException("cantidad debe ser mayor a 0");
        }
        if (req.getUbicacion() == null || req.getUbicacion().isBlank()) {
            throw new IllegalArgumentException("ubicacion requerida");
        }
        if (req.getUsuarioNombre() == null || req.getUsuarioNombre().isBlank()) {
            throw new IllegalArgumentException("usuarioNombre requerido");
        }
    }

    private MovimientoCajaDTO toDTO(MovimientoCaja movimiento) {
        MovimientoCajaDTO dto = new MovimientoCajaDTO();
        dto.setId(movimiento.getId());
        dto.setCajaId(movimiento.getCaja().getId());
        dto.setCodigoCaja(movimiento.getCaja().getCodigo());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento().name());
        dto.setCantidad(movimiento.getCantidad());
        dto.setUbicacion(movimiento.getUbicacion());
        dto.setUsuarioNombre(movimiento.getUsuarioNombre());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setObservaciones(movimiento.getObservaciones());
        return dto;
    }
}