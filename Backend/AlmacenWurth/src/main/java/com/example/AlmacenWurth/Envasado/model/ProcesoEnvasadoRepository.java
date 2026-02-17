package com.example.AlmacenWurth.Envasado.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProcesoEnvasadoRepository extends JpaRepository<ProcesoEnvasado, Long> {
    List<ProcesoEnvasado> findByHoraFinIsNullOrderByHoraInicioAsc();
    boolean existsByProductoIdAndHoraFinIsNull(Long productoId);
    // Opcional: si también quieres evitar que un envasador tenga 2 procesos activos
    boolean existsByEnvasadorIdAndHoraFinIsNull(Long envasadorId);
}
