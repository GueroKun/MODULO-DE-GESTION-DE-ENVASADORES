package com.example.AlmacenWurth.Envasador.controller;

import com.example.AlmacenWurth.Envasador.model.Envasador;
import com.example.AlmacenWurth.Envasador.model.EnvasadorDTO;
import com.example.AlmacenWurth.Envasador.model.EnvasadorRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvasadorService {

    private final EnvasadorRepository envasadorRepository;

    public EnvasadorService(EnvasadorRepository envasadorRepository) {
        this.envasadorRepository = envasadorRepository;
    }

    @Transactional
    public EnvasadorDTO crear(EnvasadorDTO req) {
        if (req.getNombre() == null || req.getNombre().isBlank()) throw new IllegalArgumentException("nombre requerido");
        Envasador e = new Envasador();
        e.setNombre(req.getNombre().trim());
        return toDTO(envasadorRepository.save(e));
    }

    @Transactional(readOnly = true)
    public List<EnvasadorDTO> listar() {
        return envasadorRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public EnvasadorDTO editar(Long id, EnvasadorDTO req) {
        if (id == null) throw new IllegalArgumentException("id requerido");
        if (req == null) throw new IllegalArgumentException("body requerido");
        if (req.getNombre() == null || req.getNombre().isBlank())
            throw new IllegalArgumentException("nombre requerido");

        Envasador e = envasadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Envasador no encontrado: " + id));

        e.setNombre(req.getNombre().trim());

        return toDTO(envasadorRepository.save(e));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!envasadorRepository.existsById(id)) throw new NotFoundException("Envasador no encontrado: " + id);
        envasadorRepository.deleteById(id); // HARD DELETE como pediste
    }

    @Transactional(readOnly = true)
    public Envasador obtenerOrThrow(Long id) {
        return envasadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Envasador no encontrado: " + id));
    }

    private EnvasadorDTO toDTO(Envasador e) {
        EnvasadorDTO dto = new EnvasadorDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        return dto;
    }
}
