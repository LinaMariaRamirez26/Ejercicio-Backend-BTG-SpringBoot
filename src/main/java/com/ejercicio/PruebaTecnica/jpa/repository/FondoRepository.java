package com.ejercicio.PruebaTecnica.jpa.repository;

import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link FondoEntity}.
 */

public interface FondoRepository extends MongoRepository<FondoEntity, String> {
}
