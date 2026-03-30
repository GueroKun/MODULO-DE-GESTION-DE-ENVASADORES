package com.example.AlmacenWurth.Rack.controller;

import com.example.AlmacenWurth.ArticuloTarima.model.ArticuloTarima;
import com.example.AlmacenWurth.ArticuloTarima.model.ArticuloTarimaRepository;
import com.example.AlmacenWurth.Rack.model.*;

import com.example.AlmacenWurth.Tarima.model.Tarima;
import com.example.AlmacenWurth.Tarima.model.TarimaRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RackService {

    private final RackRepository rackRepository;
    private final TarimaRepository tarimaRepository;
    private final ArticuloTarimaRepository articuloTarimaRepository;

    public RackService(RackRepository rackRepository,
                       TarimaRepository tarimaRepository,
                       ArticuloTarimaRepository articuloTarimaRepository) {
        this.rackRepository = rackRepository;
        this.tarimaRepository = tarimaRepository;
        this.articuloTarimaRepository = articuloTarimaRepository;
    }

    @Transactional
    public RackDTO crear(RackDTO req) {
        if (req.getUbicacion() == null || req.getUbicacion().isBlank()) {
            throw new IllegalArgumentException("ubicacion requerida");
        }
        if (req.getLimiteTarimas() == null || req.getLimiteTarimas() <= 0) {
            throw new IllegalArgumentException("limiteTarimas debe ser mayor a 0");
        }

        String ubicacion = req.getUbicacion().trim();
        if (rackRepository.existsByUbicacion(ubicacion)) {
            throw new IllegalArgumentException("Ya existe un rack con esa ubicacion");
        }

        Rack rack = new Rack();
        rack.setUbicacion(ubicacion);
        rack.setLimiteTarimas(req.getLimiteTarimas());

        return toDTO(rackRepository.save(rack));
    }

    @Transactional(readOnly = true)
    public RackDetalleDTO obtenerDetalle(Long rackId) {
        Rack rack = obtenerOrThrow(rackId);

        List<Tarima> tarimas = tarimaRepository.findByRackId(rackId);

        List<TarimaRackDetalleDTO> tarimasDTO = tarimas.stream().map(tarima -> {
            TarimaRackDetalleDTO tarimaDTO = new TarimaRackDetalleDTO();
            tarimaDTO.setId(tarima.getId());
            tarimaDTO.setNumeroReferencia(tarima.getNumeroReferencia());

            List<ArticuloRackDetalleDTO> articulosDTO = articuloTarimaRepository.findByTarimaId(tarima.getId())
                    .stream()
                    .map(this::toArticuloRackDetalleDTO)
                    .toList();

            tarimaDTO.setArticulos(articulosDTO);
            return tarimaDTO;
        }).toList();

        RackDetalleDTO dto = new RackDetalleDTO();
        dto.setId(rack.getId());
        dto.setUbicacion(rack.getUbicacion());
        dto.setLimiteTarimas(rack.getLimiteTarimas());
        dto.setTotalTarimas((long) tarimas.size());
        dto.setTarimas(tarimasDTO);

        return dto;
    }

    private ArticuloRackDetalleDTO toArticuloRackDetalleDTO(ArticuloTarima articulo) {
        ArticuloRackDetalleDTO dto = new ArticuloRackDetalleDTO();
        dto.setId(articulo.getId());
        dto.setCodigo(articulo.getCodigo());
        dto.setCantidad(articulo.getCantidad());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<RackDetalleDTO> listarDetalleCompleto() {
        List<Rack> racks = rackRepository.findAll();

        return racks.stream().map(rack -> {
            List<Tarima> tarimas = tarimaRepository.findByRackId(rack.getId());

            List<TarimaRackDetalleDTO> tarimasDTO = tarimas.stream().map(tarima -> {
                TarimaRackDetalleDTO tarimaDTO = new TarimaRackDetalleDTO();
                tarimaDTO.setId(tarima.getId());
                tarimaDTO.setNumeroReferencia(tarima.getNumeroReferencia());

                List<ArticuloRackDetalleDTO> articulosDTO = articuloTarimaRepository.findByTarimaId(tarima.getId())
                        .stream()
                        .map(this::toArticuloRackDetalleDTO)
                        .toList();

                tarimaDTO.setArticulos(articulosDTO);
                return tarimaDTO;
            }).toList();

            RackDetalleDTO dto = new RackDetalleDTO();
            dto.setId(rack.getId());
            dto.setUbicacion(rack.getUbicacion());
            dto.setLimiteTarimas(rack.getLimiteTarimas());
            dto.setTotalTarimas((long) tarimas.size());
            dto.setTarimas(tarimasDTO);

            return dto;
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<RackDTO> listar() {
        return rackRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Rack obtenerOrThrow(Long id) {
        return rackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rack no encontrado: " + id));
    }

    @Transactional
    public RackDTO editar(Long id, RackDTO req) {
        Rack rack = obtenerOrThrow(id);

        if (req.getUbicacion() != null && !req.getUbicacion().isBlank()) {
            String nuevaUbicacion = req.getUbicacion().trim();
            if (!nuevaUbicacion.equals(rack.getUbicacion()) && rackRepository.existsByUbicacion(nuevaUbicacion)) {
                throw new IllegalArgumentException("Ya existe un rack con esa ubicacion");
            }
            rack.setUbicacion(nuevaUbicacion);
        }

        if (req.getLimiteTarimas() != null) {
            if (req.getLimiteTarimas() <= 0) {
                throw new IllegalArgumentException("limiteTarimas debe ser mayor a 0");
            }

            long totalTarimas = tarimaRepository.countByRackId(rack.getId());
            if (req.getLimiteTarimas() < totalTarimas) {
                throw new IllegalArgumentException("El limite no puede ser menor al numero actual de tarimas");
            }

            rack.setLimiteTarimas(req.getLimiteTarimas());
        }

        return toDTO(rackRepository.save(rack));
    }

    @Transactional
    public void eliminar(Long id) {
        Rack rack = obtenerOrThrow(id);
        long totalTarimas = tarimaRepository.countByRackId(rack.getId());

        if (totalTarimas > 0) {
            throw new IllegalArgumentException("No se puede eliminar el rack porque tiene tarimas asociadas");
        }

        rackRepository.delete(rack);
    }

    private RackDTO toDTO(Rack rack) {
        RackDTO dto = new RackDTO();
        dto.setId(rack.getId());
        dto.setUbicacion(rack.getUbicacion());
        dto.setLimiteTarimas(rack.getLimiteTarimas());
        dto.setTotalTarimas(tarimaRepository.countByRackId(rack.getId()));
        return dto;
    }
}