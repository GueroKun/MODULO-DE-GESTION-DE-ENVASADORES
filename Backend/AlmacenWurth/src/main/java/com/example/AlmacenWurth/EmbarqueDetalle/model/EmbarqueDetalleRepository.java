package com.example.AlmacenWurth.EmbarqueDetalle.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmbarqueDetalleRepository extends JpaRepository<EmbarqueDetalle, Long> {
    List<EmbarqueDetalle> findByEmbarqueId(Long embarqueId);
    void deleteByEmbarqueId(Long embarqueId);
}