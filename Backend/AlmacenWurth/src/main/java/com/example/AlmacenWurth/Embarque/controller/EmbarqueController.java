package com.example.AlmacenWurth.Embarque.controller;

import com.example.AlmacenWurth.Embarque.model.EmbarqueDTO;
import com.example.AlmacenWurth.Embarque.model.EmbarquePreviewDTO;
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

    @PostMapping(value = "/preview", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public EmbarquePreviewDTO generarPreview(@RequestParam("file") MultipartFile file,
                                             @RequestParam(required = false) Long usuarioId) {
        return embarqueService.generarPreview(file, usuarioId);
    }

    @GetMapping("/preview/{previewId}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public EmbarquePreviewDTO obtenerPreview(@PathVariable String previewId) {
        return embarqueService.obtenerPreview(previewId);
    }

    @PostMapping("/confirmar/{previewId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public EmbarqueDTO confirmarPreview(@PathVariable String previewId) {
        return embarqueService.confirmarPreview(previewId);
    }

    @DeleteMapping("/preview/{previewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS')")
    public void cancelarPreview(@PathVariable String previewId) {
        embarqueService.cancelarPreview(previewId);
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