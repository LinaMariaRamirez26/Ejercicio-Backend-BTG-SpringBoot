package com.ejercicio.PruebaTecnica.controller;

import com.ejercicio.PruebaTecnica.dto.RespuestaCancelacionFondo;
import com.ejercicio.PruebaTecnica.jpa.entity.TransaccionEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import com.ejercicio.PruebaTecnica.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Clase donde se implementan los servicios encargados de realizar las operaciones
 * relacionadas con el manejo de usuarios y sus funciones
 *
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Controlador encargado de obteener los ususarios registrados
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Optional<UsuarioEntity> obtenerUsuario(@PathVariable String id) {
        return usuarioService.obtenerUsuario(id);
    }

    /**
     * Contorlador encargado de realizar la suscripcion a los fondos
     * @param id
     * @param fondoId
     * @param monto
     * @return
     */
    @PostMapping("/{id}/fondos/{fondoId}")
    public UsuarioEntity suscribirAFondo(
            @PathVariable String id,
            @PathVariable String fondoId,
            @RequestParam Double monto) {
        return usuarioService.suscribirAFondo(id, fondoId, monto);
    }

    /**
     * Controlador encargado de realizar el proceso de cancelacion a los fondos
     * @param usuarioId
     * @param fondoId
     * @return
     */
    @DeleteMapping("/{usuarioId}/fondos/{fondoId}")
    public ResponseEntity<RespuestaCancelacionFondo> cancelarFondo(
            @PathVariable String usuarioId,
            @PathVariable String fondoId) {
        RespuestaCancelacionFondo response = usuarioService.cancelarFondo(usuarioId, fondoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Controlador encargado de realizar la busqueda del historial de transacciones
     * @param usuarioId
     * @return
     */
    @GetMapping("/{usuarioId}/transacciones")
    public ResponseEntity<Page<TransaccionEntity>> obtenerHistorialTransacciones(
            @PathVariable String usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        Page<TransaccionEntity> historial = usuarioService.obtenerHistorialTransacciones(usuarioId, pageable);

        return ResponseEntity.ok(historial);
    }




}
