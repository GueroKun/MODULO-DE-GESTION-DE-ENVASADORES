package com.example.AlmacenWurth.StockCaja.controller;

import com.example.AlmacenWurth.StockCaja.model.StockCaja;
import com.example.AlmacenWurth.StockCaja.model.StockCajaDTO;
import com.example.AlmacenWurth.StockCaja.model.StockCajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockCajaService {

    private final StockCajaRepository stockCajaRepository;

    public StockCajaService(StockCajaRepository stockCajaRepository) {
        this.stockCajaRepository = stockCajaRepository;
    }

    @Transactional(readOnly = true)
    public List<StockCajaDTO> listar() {
        return stockCajaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<StockCajaDTO> listarPorCaja(Long cajaId) {
        return stockCajaRepository.findByCajaId(cajaId).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<StockCajaDTO> buscarPorUbicacion(String ubicacion) {
        return stockCajaRepository.findByUbicacionContainingIgnoreCase(ubicacion).stream().map(this::toDTO).toList();
    }

    private StockCajaDTO toDTO(StockCaja stockCaja) {
        StockCajaDTO dto = new StockCajaDTO();
        dto.setId(stockCaja.getId());
        dto.setCajaId(stockCaja.getCaja().getId());
        dto.setCodigoCaja(stockCaja.getCaja().getCodigo());
        dto.setUbicacion(stockCaja.getUbicacion());
        dto.setStockActual(stockCaja.getStockActual());
        dto.setCajaActiva(stockCaja.getCaja().getActivo());
        return dto;
    }
}