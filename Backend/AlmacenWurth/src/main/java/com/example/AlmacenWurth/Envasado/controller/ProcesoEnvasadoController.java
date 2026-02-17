package com.example.AlmacenWurth.Envasado.controller;

import com.example.AlmacenWurth.Envasado.model.ProcesoEnvasadoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envasado")
public class ProcesoEnvasadoController {

    private final ProcesoEnvasadoService service;

    public ProcesoEnvasadoController(ProcesoEnvasadoService service) {
        this.service = service;
    }

    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ENVASADOR','ADMIN')")
    public ProcesoEnvasadoDTO iniciar(@RequestBody com.example.AlmacenWurth.Envasado.model.IniciarProcesoRequestDTO req) {
        return service.iniciar(req.getEnvasadorId(), req.getCodigoProducto());
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('ENVASADOR','ADMIN')")
    public ProcesoEnvasadoDTO finalizar(@PathVariable Long id) {
        return service.finalizar(id);
    }

    @GetMapping("/en-proceso")
    @PreAuthorize("hasAnyRole('ENVASADOR','ADMIN','MONTACARGAS')")
    public List<ProcesoEnvasadoDTO> enProceso() {
        return service.enProceso();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ENVASADOR','ADMIN','MONTACARGAS')")
    public ProcesoEnvasadoDTO obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}
