package com.example.AlmacenWurth.Envasado.controller;

import com.example.AlmacenWurth.Envasado.model.IniciarProcesoRequestDTO;
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
    public ProcesoEnvasadoDTO iniciar(@RequestBody IniciarProcesoRequestDTO req) {
        return service.iniciar(
                req.getEnvasadorId(),
                req.getCodigoProducto(),
                req.getCantidadAsignada(),
                req.getMinimoEnvasado()
        );
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ProcesoEnvasadoDTO finalizar(@PathVariable Long id) {
        return service.finalizar(id);
    }

    @GetMapping("/en-proceso")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ProcesoEnvasadoDTO> enProceso() {
        return service.enProceso();
    }

    @GetMapping("/finalizados")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ProcesoEnvasadoDTO> finalizados() {
        return service.finalizados();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ProcesoEnvasadoDTO obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}