package com.example.AlmacenWurth.EmbarqueDetalle.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmbarqueDetalleRepository extends JpaRepository<EmbarqueDetalle, Long> {
    List<EmbarqueDetalle> findByEmbarqueId(Long embarqueId);
    void deleteByEmbarqueId(Long embarqueId);
    @Query("SELECT e FROM EmbarqueDetalle e " +
            "WHERE e.embarque.id = :embarqueId " +
            "ORDER BY CASE " +
            "WHEN e.abc IS NULL OR e.abc = '' THEN 'Z' " +
            "ELSE SUBSTRING(e.abc, 1, 1) " +
            "END ASC")
    List<EmbarqueDetalle> findByEmbarqueIdOrderByPrimeraLetraAbcAsc(Long embarqueId);
}