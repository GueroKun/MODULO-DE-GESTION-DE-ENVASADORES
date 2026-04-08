package com.example.AlmacenWurth.MovimientoCaja.controller;

import com.example.AlmacenWurth.MovimientoCaja.model.MovimientoCajaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos-caja")
public class MovimientoCajaController {

    private final MovimientoCajaService movimientoCajaService;

    public MovimientoCajaController(MovimientoCajaService movimientoCajaService) {
        this.movimientoCajaService = movimientoCajaService;
    }

    @PostMapping("/entrada")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public MovimientoCajaDTO registrarEntrada(@RequestBody MovimientoCajaDTO req) {
        return movimientoCajaService.registrarEntrada(req);
    }

    @PostMapping("/salida")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public MovimientoCajaDTO registrarSalida(@RequestBody MovimientoCajaDTO req) {
        return movimientoCajaService.registrarSalida(req);
    }

    @GetMapping("/entradas")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<MovimientoCajaDTO> listarEntradas() {
        return movimientoCajaService.listarEntradas();
    }

    @GetMapping("/salidas")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<MovimientoCajaDTO> listarSalidas() {
        return movimientoCajaService.listarSalidas();
    }

    @GetMapping("/caja/{cajaId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<MovimientoCajaDTO> historialPorCaja(@PathVariable Long cajaId) {
        return movimientoCajaService.historialPorCaja(cajaId);
    }
}