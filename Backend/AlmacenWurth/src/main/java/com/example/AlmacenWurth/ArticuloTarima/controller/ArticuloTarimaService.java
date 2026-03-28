package com.example.AlmacenWurth.ArticuloTarima.controller;

import com.example.AlmacenWurth.ArticuloTarima.model.ArticuloTarima;
import com.example.AlmacenWurth.ArticuloTarima.model.ArticuloTarimaDTO;
import com.example.AlmacenWurth.ArticuloTarima.model.ArticuloTarimaRepository;
import com.example.AlmacenWurth.Tarima.model.Tarima;
import com.example.AlmacenWurth.Tarima.model.TarimaRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticuloTarimaService {

    private final ArticuloTarimaRepository articuloTarimaRepository;
    private final TarimaRepository tarimaRepository;

    public ArticuloTarimaService(ArticuloTarimaRepository articuloTarimaRepository,
                                 TarimaRepository tarimaRepository) {
        this.articuloTarimaRepository = articuloTarimaRepository;
        this.tarimaRepository = tarimaRepository;
    }

    @Transactional
    public ArticuloTarimaDTO crear(ArticuloTarimaDTO req) {
        if (req.getTarimaId() == null) {
            throw new IllegalArgumentException("tarimaId requerido");
        }
        if (req.getCodigo() == null || req.getCodigo().isBlank()) {
            throw new IllegalArgumentException("codigo requerido");
        }
        if (req.getCantidad() == null || req.getCantidad() <= 0) {
            throw new IllegalArgumentException("cantidad debe ser mayor a 0");
        }

        Tarima tarima = tarimaRepository.findById(req.getTarimaId())
                .orElseThrow(() -> new NotFoundException("Tarima no encontrada: " + req.getTarimaId()));

        ArticuloTarima articulo = new ArticuloTarima();
        articulo.setTarima(tarima);
        articulo.setCodigo(req.getCodigo().trim());
        articulo.setCantidad(req.getCantidad());

        return toDTO(articuloTarimaRepository.save(articulo));
    }

    @Transactional(readOnly = true)
    public List<ArticuloTarimaDTO> listarPorTarima(Long tarimaId) {
        return articuloTarimaRepository.findByTarimaId(tarimaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public ArticuloTarimaDTO editar(Long id, ArticuloTarimaDTO req) {
        ArticuloTarima articulo = articuloTarimaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ArticuloTarima no encontrado: " + id));

        if (req.getCodigo() != null && !req.getCodigo().isBlank()) {
            articulo.setCodigo(req.getCodigo().trim());
        }

        if (req.getCantidad() != null) {
            if (req.getCantidad() <= 0) {
                throw new IllegalArgumentException("cantidad debe ser mayor a 0");
            }
            articulo.setCantidad(req.getCantidad());
        }

        return toDTO(articuloTarimaRepository.save(articulo));
    }

    @Transactional
    public void eliminar(Long id) {
        ArticuloTarima articulo = articuloTarimaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ArticuloTarima no encontrado: " + id));

        articuloTarimaRepository.delete(articulo);
    }

    private ArticuloTarimaDTO toDTO(ArticuloTarima a) {
        ArticuloTarimaDTO dto = new ArticuloTarimaDTO();
        dto.setId(a.getId());
        dto.setTarimaId(a.getTarima().getId());
        dto.setNumeroReferenciaTarima(a.getTarima().getNumeroReferencia());
        dto.setCodigo(a.getCodigo());
        dto.setCantidad(a.getCantidad());
        return dto;
    }
}