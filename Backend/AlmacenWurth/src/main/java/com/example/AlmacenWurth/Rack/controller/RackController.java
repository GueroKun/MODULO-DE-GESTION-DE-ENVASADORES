package com.example.AlmacenWurth.Rack.controller;

import com.example.AlmacenWurth.Rack.model.RackDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racks")
public class RackController {

    private final RackService rackService;

    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public RackDTO crear(@RequestBody RackDTO req) {
        return rackService.crear(req);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public List<RackDTO> listar() {
        return rackService.listar();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public RackDTO editar(@PathVariable Long id, @RequestBody RackDTO req) {
        return rackService.editar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        rackService.eliminar(id);
    }
}