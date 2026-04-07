package com.example.AlmacenWurth.Caja.controller;

import com.example.AlmacenWurth.Caja.model.CajaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cajas")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CajaDTO crear(@RequestBody CajaDTO req) {
        return cajaService.crear(req);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<CajaDTO> listar() {
        return cajaService.listar();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CajaDTO actualizar(@PathVariable Long id, @RequestBody CajaDTO req) {
        return cajaService.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        cajaService.eliminar(id);
    }
}