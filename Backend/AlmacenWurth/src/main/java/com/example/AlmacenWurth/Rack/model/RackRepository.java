package com.example.AlmacenWurth.Rack.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RackRepository extends JpaRepository<Rack, Long> {
    boolean existsByUbicacion(String ubicacion);
    Optional<Rack> findByUbicacion(String ubicacion);
}