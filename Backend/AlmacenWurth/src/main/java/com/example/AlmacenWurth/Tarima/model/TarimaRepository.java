package com.example.AlmacenWurth.Tarima.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarimaRepository extends JpaRepository<Tarima, Long> {
    boolean existsByNumeroReferencia(String numeroReferencia);
    Optional<Tarima> findByNumeroReferencia(String numeroReferencia);
    List<Tarima> findByRackId(Long rackId);
    long countByRackId(Long rackId);
}