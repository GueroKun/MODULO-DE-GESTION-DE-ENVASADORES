package com.example.AlmacenWurth.StockCaja.controller;

import com.example.AlmacenWurth.StockCaja.model.StockCajaDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-cajas")
public class StockCajaController {

    private final StockCajaService stockCajaService;

    public StockCajaController(StockCajaService stockCajaService) {
        this.stockCajaService = stockCajaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<StockCajaDTO> listar() {
        return stockCajaService.listar();
    }

    @GetMapping("/caja/{cajaId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<StockCajaDTO> listarPorCaja(@PathVariable Long cajaId) {
        return stockCajaService.listarPorCaja(cajaId);
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<StockCajaDTO> buscarPorUbicacion(@RequestParam String ubicacion) {
        return stockCajaService.buscarPorUbicacion(ubicacion);
    }
}