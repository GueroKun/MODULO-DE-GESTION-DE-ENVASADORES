package com.example.AlmacenWurth.Caja.controller;

import com.example.AlmacenWurth.Caja.model.Caja;
import com.example.AlmacenWurth.Caja.model.CajaDTO;
import com.example.AlmacenWurth.Caja.model.CajaRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CajaService {

    private final CajaRepository cajaRepository;

    public CajaService(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Transactional
    public CajaDTO crear(CajaDTO req) {
        if (req.getCodigo() == null || req.getCodigo().isBlank()) {
            throw new IllegalArgumentException("codigo requerido");
        }
        if (req.getActivo() == null) {
            throw new IllegalArgumentException("activo requerido");
        }

        String codigo = req.getCodigo().trim();

        if (cajaRepository.existsByCodigo(codigo)) {
            throw new IllegalArgumentException("Ya existe una caja con codigo: " + codigo);
        }

        Caja caja = new Caja();
        caja.setCodigo(codigo);
        caja.setActivo(req.getActivo());

        return toDTO(cajaRepository.save(caja));
    }

    @Transactional(readOnly = true)
    public List<CajaDTO> listar() {
        return cajaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Caja obtenerOrThrow(Long id) {
        return cajaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Caja no encontrada: " + id));
    }

    @Transactional
    public CajaDTO actualizar(Long id, CajaDTO req) {
        Caja caja = obtenerOrThrow(id);

        if (req.getCodigo() != null && !req.getCodigo().isBlank()) {
            String nuevoCodigo = req.getCodigo().trim();
            if (!nuevoCodigo.equals(caja.getCodigo()) && cajaRepository.existsByCodigo(nuevoCodigo)) {
                throw new IllegalArgumentException("Ya existe una caja con codigo: " + nuevoCodigo);
            }
            caja.setCodigo(nuevoCodigo);
        }

        if (req.getActivo() != null) {
            caja.setActivo(req.getActivo());
        }

        return toDTO(cajaRepository.save(caja));
    }

    @Transactional
    public void eliminar(Long id) {
        Caja caja = obtenerOrThrow(id);
        cajaRepository.delete(caja);
    }

    private CajaDTO toDTO(Caja caja) {
        CajaDTO dto = new CajaDTO();
        dto.setId(caja.getId());
        dto.setCodigo(caja.getCodigo());
        dto.setActivo(caja.getActivo());
        return dto;
    }
}