package com.example.AlmacenWurth.StockCaja.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockCajaRepository extends JpaRepository<StockCaja, Long> {
    Optional<StockCaja> findByCajaIdAndUbicacion(Long cajaId, String ubicacion);
    List<StockCaja> findByCajaId(Long cajaId);
    List<StockCaja> findByUbicacionContainingIgnoreCase(String ubicacion);
}