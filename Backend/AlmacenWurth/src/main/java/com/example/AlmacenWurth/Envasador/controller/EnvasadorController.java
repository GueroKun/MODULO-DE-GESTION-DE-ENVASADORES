package com.example.AlmacenWurth.Envasador.controller;

import com.example.AlmacenWurth.Envasador.model.EnvasadorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envasadores")
public class EnvasadorController {

    private final EnvasadorService envasadorService;

    public EnvasadorController(EnvasadorService envasadorService) {
        this.envasadorService = envasadorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public EnvasadorDTO crear(@RequestBody EnvasadorDTO req) {
        return envasadorService.crear(req);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENVASADOR')")
    public List<EnvasadorDTO> listar() {
        return envasadorService.listar();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EnvasadorDTO editar(@PathVariable Long id, @RequestBody EnvasadorDTO req) {
        return envasadorService.editar(id, req);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        envasadorService.eliminar(id);
    }
}
