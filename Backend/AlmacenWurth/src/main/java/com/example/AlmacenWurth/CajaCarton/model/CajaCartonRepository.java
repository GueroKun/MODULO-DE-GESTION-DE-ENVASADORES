package com.example.AlmacenWurth.CajaCarton.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CajaCartonRepository extends JpaRepository<CajaCarton, Long> {

    Optional<CajaCarton> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CajaCarton> findByActivoTrue();

    List<CajaCarton> findByActivoTrueAndStockActualLessThanEqual(Integer stockMinimo);
}