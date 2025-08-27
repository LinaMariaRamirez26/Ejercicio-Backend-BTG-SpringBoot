package com.ejercicio.PruebaTecnica.jpa.repository;

import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FondoRepository extends MongoRepository<FondoEntity, String> {
}
