package com.ejercicio.PruebaTecnica.jpa.repository;

import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository  extends MongoRepository<UsuarioEntity, String> {
}
