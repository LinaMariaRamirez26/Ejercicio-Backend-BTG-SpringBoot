package com.ejercicio.PruebaTecnica.controller;

import com.ejercicio.PruebaTecnica.dto.RespuestaCancelacionFondo;
import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import com.ejercicio.PruebaTecnica.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    @DisplayName("Test obtenerUsuario exitoso (estilo directo)")
    void obtenerUsuarioTest() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId("1");
        usuario.setNombre("Juan");
        usuario.setSaldo(1000.0);

        when(usuarioService.obtenerUsuario("1")).thenReturn(Optional.of(usuario));

        Optional<UsuarioEntity> resultado = usuarioController.obtenerUsuario("1");

        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("Test cancelar fondo exitoso")
    void cancelarFondoTest() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId("1");
        RespuestaCancelacionFondo respuesta = new RespuestaCancelacionFondo(usuario, "FondoEjemplo");
        when(usuarioService.cancelarFondo("1", "101")).thenReturn(respuesta);
        ResponseEntity<RespuestaCancelacionFondo> response =
                usuarioController.cancelarFondo("1", "101");
        assertNotNull(response);
    }
}
