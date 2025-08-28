package com.ejercicio.PruebaTecnica.controller;

import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import com.ejercicio.PruebaTecnica.service.FondoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Clase donde se implementan los servicios encargados de realizar las operaciones
 * relacionadas con el manejo de fondos
 *
 */
@RestController
@RequestMapping("/api/fondos")
@CrossOrigin(origins = "*")
public class FondoController {

    private final FondoService fondoService;

    public FondoController(FondoService fondoService) {
        this.fondoService = fondoService;
    }

    /**
     * Controlador encargado de listar todos los fondos disponibles para la inversion
     * @return
     */
    @GetMapping
    public List<FondoEntity> listarFondos() {
        return fondoService.obtenerTodos();
    }

    /**
     * Controlador encargado de buscar los fondos por su codigo
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public Optional<FondoEntity> obtenerPorId(@PathVariable String id) {
        return fondoService.obtenerPorId(id);
    }
}
