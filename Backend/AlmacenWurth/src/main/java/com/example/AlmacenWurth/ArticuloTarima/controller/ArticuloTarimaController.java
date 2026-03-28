package com.example.AlmacenWurth.ArticuloTarima.controller;

import com.example.AlmacenWurth.ArticuloTarima.model.ArticuloTarimaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos-tarima")
public class ArticuloTarimaController {

    private final ArticuloTarimaService articuloTarimaService;

    public ArticuloTarimaController(ArticuloTarimaService articuloTarimaService) {
        this.articuloTarimaService = articuloTarimaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public ArticuloTarimaDTO crear(@RequestBody ArticuloTarimaDTO req) {
        return articuloTarimaService.crear(req);
    }

    @GetMapping("/tarima/{tarimaId}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public List<ArticuloTarimaDTO> listarPorTarima(@PathVariable Long tarimaId) {
        return articuloTarimaService.listarPorTarima(tarimaId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public ArticuloTarimaDTO editar(@PathVariable Long id, @RequestBody ArticuloTarimaDTO req) {
        return articuloTarimaService.editar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public void eliminar(@PathVariable Long id) {
        articuloTarimaService.eliminar(id);
    }
}