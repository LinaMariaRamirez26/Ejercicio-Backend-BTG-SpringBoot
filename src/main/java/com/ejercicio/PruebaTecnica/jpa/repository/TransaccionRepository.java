package com.ejercicio.PruebaTecnica.jpa.repository;

import com.ejercicio.PruebaTecnica.jpa.entity.TransaccionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransaccionRepository extends MongoRepository<TransaccionEntity, String> {

    List<TransaccionEntity> findByUsuarioIdAndFondoId(String usuarioId, String fondoId);

    List<TransaccionEntity> findByUsuarioIdOrderByFechaDesc(String usuarioId);
}
