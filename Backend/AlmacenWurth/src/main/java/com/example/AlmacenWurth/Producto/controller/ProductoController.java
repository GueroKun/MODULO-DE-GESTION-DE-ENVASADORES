package com.example.AlmacenWurth.Producto.controller;

import com.example.AlmacenWurth.Producto.model.ProductoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO crear(@RequestBody ProductoDTO req) {
        return productoService.crear(req);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENVASADOR','MONTACARGAS')")
    public List<ProductoDTO> listar() {
        return productoService.listar();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO actualizar(@PathVariable Long id, @RequestBody ProductoDTO req) {
        return productoService.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }

}
