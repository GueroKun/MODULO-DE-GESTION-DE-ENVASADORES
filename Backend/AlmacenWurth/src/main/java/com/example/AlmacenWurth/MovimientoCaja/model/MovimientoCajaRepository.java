package com.example.AlmacenWurth.MovimientoCaja.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByTipoMovimientoOrderByFechaMovimientoDesc(MovimientoCaja.TipoMovimiento tipoMovimiento);
    List<MovimientoCaja> findByCajaIdOrderByFechaMovimientoDesc(Long cajaId);
}