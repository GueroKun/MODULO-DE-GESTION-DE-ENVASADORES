package com.example.AlmacenWurth.Caja.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {
    boolean existsByCodigo(String codigo);
    Optional<Caja> findByCodigo(String codigo);
}