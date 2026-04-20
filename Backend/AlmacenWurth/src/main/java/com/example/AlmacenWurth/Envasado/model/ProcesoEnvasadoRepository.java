package com.example.AlmacenWurth.Envasado.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcesoEnvasadoRepository extends JpaRepository<ProcesoEnvasado, Long> {

    List<ProcesoEnvasado> findByHoraFinIsNullOrderByHoraInicioAsc();

    boolean existsByEnvasadorIdAndHoraFinIsNull(Long envasadorId);

    List<ProcesoEnvasado> findByProductoId(Long productoId);

    List<ProcesoEnvasado> findByEnvasadorId(Long envasadorId);

    List<ProcesoEnvasado> findByHoraFinIsNotNullOrderByHoraFinDesc();

    @Query("""
        SELECT COALESCE(SUM(p.cantidadAsignada), 0)
        FROM ProcesoEnvasado p
        WHERE p.producto.id = :productoId
          AND p.horaFin IS NULL
    """)
    Integer sumarCantidadAsignadaActivaPorProducto(@Param("productoId") Long productoId);

    long countByProductoIdAndHoraFinIsNull(Long productoId);
}