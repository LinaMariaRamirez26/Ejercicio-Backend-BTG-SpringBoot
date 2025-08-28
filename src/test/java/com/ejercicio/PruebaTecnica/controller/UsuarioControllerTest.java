package com.ejercicio.PruebaTecnica.controller;

import com.ejercicio.PruebaTecnica.dto.RespuestaCancelacionFondo;
import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import com.ejercicio.PruebaTecnica.service.UsuarioService;
import com.ejercicio.PruebaTecnica.utilies.Constantes;
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
    @DisplayName("Test obtener usuario exitoso")
    void obtenerUsuarioTest() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(Constantes.CODIGO_USUARIO_PRUEBA);
        usuario.setNombre(Constantes.NOMBRE_USUARIO_PRUEBA);
        usuario.setSaldo(1000.0);

        when(usuarioService.obtenerUsuario(Constantes.CODIGO_USUARIO_PRUEBA)).thenReturn(Optional.of(usuario));

        Optional<UsuarioEntity> resultado = usuarioController.obtenerUsuario(Constantes.CODIGO_USUARIO_PRUEBA);

        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("Test cancelar fondo exitoso")
    void cancelarFondoTest() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(Constantes.CODIGO_USUARIO_PRUEBA);
        RespuestaCancelacionFondo respuesta = new RespuestaCancelacionFondo(usuario, Constantes.NOMBRE_FONDO);
        when(usuarioService.cancelarFondo(Constantes.CODIGO_USUARIO_PRUEBA, Constantes.CODIGO_FONDO)).thenReturn(respuesta);
        ResponseEntity<RespuestaCancelacionFondo> response =
                usuarioController.cancelarFondo(Constantes.CODIGO_USUARIO_PRUEBA, Constantes.CODIGO_FONDO);
        assertNotNull(response);
    }
}
