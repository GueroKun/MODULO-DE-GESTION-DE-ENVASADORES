package com.example.AlmacenWurth.Embarque.controller;

import com.example.AlmacenWurth.Embarque.model.EmbarqueDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/embarques")
public class EmbarqueController {

    private final EmbarqueService embarqueService;

    public EmbarqueController(EmbarqueService embarqueService) {
        this.embarqueService = embarqueService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public EmbarqueDTO subirArchivo(@RequestParam("file") MultipartFile file,
                                    @RequestParam(required = false) Long usuarioId) {
        return embarqueService.subirArchivo(file, usuarioId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public List<EmbarqueDTO> listar() {
        return embarqueService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public EmbarqueDTO obtener(@PathVariable Long id) {
        return embarqueService.obtener(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        embarqueService.eliminar(id);
    }
}