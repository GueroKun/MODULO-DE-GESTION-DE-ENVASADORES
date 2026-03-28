package com.example.AlmacenWurth.Tarima.controller;

import com.example.AlmacenWurth.Rack.model.Rack;
import com.example.AlmacenWurth.Rack.model.RackRepository;
import com.example.AlmacenWurth.Tarima.model.Tarima;
import com.example.AlmacenWurth.Tarima.model.TarimaDTO;
import com.example.AlmacenWurth.Tarima.model.TarimaRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarimaService {

    private final TarimaRepository tarimaRepository;
    private final RackRepository rackRepository;

    public TarimaService(TarimaRepository tarimaRepository, RackRepository rackRepository) {
        this.tarimaRepository = tarimaRepository;
        this.rackRepository = rackRepository;
    }

    @Transactional
    public TarimaDTO crear(TarimaDTO req) {
        if (req.getNumeroReferencia() == null || req.getNumeroReferencia().isBlank()) {
            throw new IllegalArgumentException("numeroReferencia requerido");
        }
        if (req.getRackId() == null) {
            throw new IllegalArgumentException("rackId requerido");
        }

        String referencia = req.getNumeroReferencia().trim();
        if (tarimaRepository.existsByNumeroReferencia(referencia)) {
            throw new IllegalArgumentException("Ya existe una tarima con ese numero de referencia");
        }

        Rack rack = rackRepository.findById(req.getRackId())
                .orElseThrow(() -> new NotFoundException("Rack no encontrado: " + req.getRackId()));

        long totalTarimas = tarimaRepository.countByRackId(rack.getId());
        if (totalTarimas >= rack.getLimiteTarimas()) {
            throw new IllegalArgumentException("El rack ya alcanzo su limite de tarimas");
        }

        Tarima tarima = new Tarima();
        tarima.setNumeroReferencia(referencia);
        tarima.setRack(rack);

        return toDTO(tarimaRepository.save(tarima));
    }

    @Transactional(readOnly = true)
    public List<TarimaDTO> listar() {
        return tarimaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<TarimaDTO> listarPorRack(Long rackId) {
        return tarimaRepository.findByRackId(rackId).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Tarima obtenerOrThrow(Long id) {
        return tarimaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarima no encontrada: " + id));
    }

    @Transactional
    public TarimaDTO mover(Long tarimaId, Long nuevoRackId) {
        Tarima tarima = obtenerOrThrow(tarimaId);

        Rack nuevoRack = rackRepository.findById(nuevoRackId)
                .orElseThrow(() -> new NotFoundException("Rack no encontrado: " + nuevoRackId));

        if (tarima.getRack().getId().equals(nuevoRackId)) {
            throw new IllegalArgumentException("La tarima ya está en ese rack");
        }

        long totalTarimasNuevoRack = tarimaRepository.countByRackId(nuevoRack.getId());
        if (totalTarimasNuevoRack >= nuevoRack.getLimiteTarimas()) {
            throw new IllegalArgumentException("El rack destino ya alcanzo su limite de tarimas");
        }

        tarima.setRack(nuevoRack);
        return toDTO(tarimaRepository.save(tarima));
    }

    @Transactional
    public void eliminar(Long id) {
        Tarima tarima = obtenerOrThrow(id);
        tarimaRepository.delete(tarima);
    }

    private TarimaDTO toDTO(Tarima t) {
        TarimaDTO dto = new TarimaDTO();
        dto.setId(t.getId());
        dto.setNumeroReferencia(t.getNumeroReferencia());
        dto.setRackId(t.getRack().getId());
        dto.setRackUbicacion(t.getRack().getUbicacion());
        return dto;
    }
}