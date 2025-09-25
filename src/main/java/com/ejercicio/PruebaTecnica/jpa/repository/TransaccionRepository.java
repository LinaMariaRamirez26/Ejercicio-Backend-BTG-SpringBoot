package com.ejercicio.PruebaTecnica.jpa.repository;

import com.ejercicio.PruebaTecnica.jpa.entity.TransaccionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link TransaccionEntity}.
 */

public interface TransaccionRepository extends MongoRepository<TransaccionEntity, String> {

    List<TransaccionEntity> findByUsuarioIdAndFondoId(String usuarioId, String fondoId);

    Page<TransaccionEntity> findByUsuarioIdOrderByFechaDesc(String usuarioId, Pageable pageable);
}
