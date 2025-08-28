package com.ejercicio.PruebaTecnica.service;

import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import com.ejercicio.PruebaTecnica.jpa.repository.FondoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que permite todas las operaciones para obtener el listado de los fondos
 */
@Service
public class FondoService {
    private final FondoRepository fondoRepository;


    public FondoService(FondoRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }

    /**
     * Servicio para obtener el listado de los fondos disponibles
     * @return
     */
    public List<FondoEntity> obtenerTodos() {
        return fondoRepository.findAll();
    }

    /**
     * Servicio que permite buscar por el codigo de los fondos disponibles
     * @param id
     * @return
     */
    public Optional<FondoEntity> obtenerPorId(String id) {
        return fondoRepository.findById(id);
    }
}
