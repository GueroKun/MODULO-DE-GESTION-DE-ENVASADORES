package com.example.AlmacenWurth.ArticuloTarima.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticuloTarimaRepository extends JpaRepository<ArticuloTarima, Long> {
    List<ArticuloTarima> findByTarimaId(Long tarimaId);
}