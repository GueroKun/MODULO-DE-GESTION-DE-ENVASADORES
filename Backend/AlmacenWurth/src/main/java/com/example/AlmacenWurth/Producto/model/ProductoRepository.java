package com.example.AlmacenWurth.Producto.model;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.codigo = :codigo")
    Optional<Producto> findByCodigoForUpdate(@Param("codigo") String codigo);

    Optional<Producto> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    @Query(value = """
        SELECT * 
        FROM producto
        ORDER BY 
            CASE prioridad
                WHEN 'ALTA' THEN 1
                WHEN 'MEDIA' THEN 2
                WHEN 'BAJA' THEN 3
                ELSE 4
            END,
            codigo ASC
        """, nativeQuery = true)
    List<Producto> findAllOrderByPrioridadAscYCodigoAsc();

}
