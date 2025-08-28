package com.ejercicio.PruebaTecnica.jpa.repository;

import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FondoRepository extends MongoRepository<FondoEntity, String> {
    List<FondoEntity> findByNombre(String nombre);

    List<FondoEntity> findByNombreContainingIgnoreCase(String nombre);
}
