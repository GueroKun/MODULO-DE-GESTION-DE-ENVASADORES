package com.example.AlmacenWurth.EmbarqueDetalle.controller;

import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalle;
import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalleDTO;
import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmbarqueDetalleService {

    private final EmbarqueDetalleRepository embarqueDetalleRepository;

    public EmbarqueDetalleService(EmbarqueDetalleRepository embarqueDetalleRepository) {
        this.embarqueDetalleRepository = embarqueDetalleRepository;
    }

    @Transactional(readOnly = true)
    public List<EmbarqueDetalleDTO> listarPorEmbarque(Long embarqueId) {
        return embarqueDetalleRepository.findByEmbarqueIdOrderByPrimeraLetraAbcAsc(embarqueId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private EmbarqueDetalleDTO toDTO(EmbarqueDetalle e) {
        EmbarqueDetalleDTO dto = new EmbarqueDetalleDTO();
        dto.setId(e.getId());
        dto.setEmbarqueId(e.getEmbarque().getId());
        dto.setCodigo(e.getCodigo());
        dto.setDescripcion(e.getDescripcion());
        dto.setCantidad(e.getCantidad());
        dto.setAbc(e.getAbc());
        return dto;
    }
}