package com.example.AlmacenWurth.EmbarqueDetalle.controller;

import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalleDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/embarque-detalles")
public class EmbarqueDetalleController {

    private final EmbarqueDetalleService embarqueDetalleService;

    public EmbarqueDetalleController(EmbarqueDetalleService embarqueDetalleService) {
        this.embarqueDetalleService = embarqueDetalleService;
    }

    @GetMapping("/embarque/{embarqueId}")
    @PreAuthorize("hasAnyRole('ADMIN','MONTACARGAS','ENVASADOR')")
    public List<EmbarqueDetalleDTO> listarPorEmbarque(@PathVariable Long embarqueId) {
        return embarqueDetalleService.listarPorEmbarque(embarqueId);
    }
}