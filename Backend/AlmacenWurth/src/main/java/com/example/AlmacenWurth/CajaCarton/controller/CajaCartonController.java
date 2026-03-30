package com.example.AlmacenWurth.CajaCarton.controller;

import com.example.AlmacenWurth.CajaCarton.model.CajaCartonDTO;
import com.example.AlmacenWurth.CajaCarton.model.MovimientoCajaCartonDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cajas-carton")
@CrossOrigin(origins = "*")
public class CajaCartonController {

    private final CajaCartonService service;

    public CajaCartonController(CajaCartonService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CajaCartonDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CajaCartonDTO>> obtenerActivas() {
        return ResponseEntity.ok(service.obtenerActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CajaCartonDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CajaCartonDTO> crear(@RequestBody CajaCartonDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CajaCartonDTO> actualizar(@PathVariable Long id, @RequestBody CajaCartonDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<CajaCartonDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(service.desactivar(id));
    }

    @PostMapping("/{id}/entrada")
    public ResponseEntity<MovimientoCajaCartonDTO> registrarEntrada(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String motivo,
            @RequestParam(required = false) String referencia) {
        return ResponseEntity.ok(service.registrarEntrada(id, cantidad, motivo, referencia));
    }

    @PostMapping("/{id}/salida")
    public ResponseEntity<MovimientoCajaCartonDTO> registrarSalida(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String motivo,
            @RequestParam(required = false) String referencia) {
        return ResponseEntity.ok(service.registrarSalida(id, cantidad, motivo, referencia));
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoCajaCartonDTO>> obtenerMovimientos() {
        return ResponseEntity.ok(service.obtenerMovimientos());
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<MovimientoCajaCartonDTO>> obtenerMovimientosPorCaja(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerMovimientosPorCaja(id));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<CajaCartonDTO>> obtenerStockBajo() {
        return ResponseEntity.ok(service.obtenerStockBajo());
    }
}