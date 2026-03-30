package com.example.AlmacenWurth.CajaCarton.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoCajaCartonRepository extends JpaRepository<MovimientoCajaCarton, Long> {

    List<MovimientoCajaCarton> findByCajaCartonIdOrderByFechaMovimientoDesc(Long cajaCartonId);

    List<MovimientoCajaCarton> findAllByOrderByFechaMovimientoDesc();
}