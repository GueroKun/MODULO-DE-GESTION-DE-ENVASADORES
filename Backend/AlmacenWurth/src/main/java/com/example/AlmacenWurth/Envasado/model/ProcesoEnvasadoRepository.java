package com.example.AlmacenWurth.Envasado.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcesoEnvasadoRepository extends JpaRepository<ProcesoEnvasado, Long> {
    List<ProcesoEnvasado> findByHoraFinIsNullOrderByHoraInicioAsc();
}
