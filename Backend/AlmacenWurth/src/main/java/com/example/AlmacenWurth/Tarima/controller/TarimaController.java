package com.example.AlmacenWurth.Tarima.controller;

import com.example.AlmacenWurth.Tarima.model.TarimaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarimas")
public class TarimaController {

    private final TarimaService tarimaService;

    public TarimaController(TarimaService tarimaService) {
        this.tarimaService = tarimaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public TarimaDTO crear(@RequestBody TarimaDTO req) {
        return tarimaService.crear(req);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public List<TarimaDTO> listar() {
        return tarimaService.listar();
    }

    @GetMapping("/rack/{rackId}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public List<TarimaDTO> listarPorRack(@PathVariable Long rackId) {
        return tarimaService.listarPorRack(rackId);
    }

    @PutMapping("/{tarimaId}/mover/{nuevoRackId}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public TarimaDTO mover(@PathVariable Long tarimaId, @PathVariable Long nuevoRackId) {
        return tarimaService.mover(tarimaId, nuevoRackId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public void eliminar(@PathVariable Long id) {
        tarimaService.eliminar(id);
    }
}