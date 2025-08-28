package com.ejercicio.PruebaTecnica.service;

import com.ejercicio.PruebaTecnica.controller.UsuarioController;
import com.ejercicio.PruebaTecnica.dto.RespuestaCancelacionFondo;
import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.TransaccionEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import com.ejercicio.PruebaTecnica.jpa.repository.FondoRepository;
import com.ejercicio.PruebaTecnica.jpa.repository.TransaccionRepository;
import com.ejercicio.PruebaTecnica.jpa.repository.UsuarioRepository;
import com.ejercicio.PruebaTecnica.utilies.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService; // tu clase que contiene los métodos

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FondoRepository fondoRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private NotificacionService notificacionService;

    private UsuarioEntity usuario;
    private FondoEntity fondo;

    @BeforeEach
    void setup() {
        usuario = new UsuarioEntity();
        usuario.setId(Constantes.CODIGO_USUARIO_PRUEBA);
        usuario.setNombre(Constantes.NOMBRE_USUARIO_PRUEBA);
        usuario.setSaldo(1000.0);
        usuario.setFondosSuscritos(new ArrayList<>());
        usuario.setPreferencia(Constantes.PREFERENCIA_PRUEBA);
        usuario.setEmail(Constantes.CORREO_PRUEBA);

        fondo = new FondoEntity();
        fondo.setId(Constantes.CODIGO_FONDO);
        fondo.setNombre(Constantes.NOMBRE_FONDO);
        fondo.setMontoMinimo(100.0);
    }
    @Test
    @DisplayName("Test suscribirAFondo falla por saldo insuficiente")
    void suscribirAFondoSaldoInsuficiente() {
        when(usuarioRepository.findById(Constantes.CODIGO_USUARIO_PRUEBA)).thenReturn(Optional.of(usuario));
        when(fondoRepository.findById(Constantes.CODIGO_FONDO)).thenReturn(Optional.of(fondo));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.suscribirAFondo(Constantes.CODIGO_USUARIO_PRUEBA, Constantes.CODIGO_FONDO, 50.000));

        assertFalse(exception.getMessage().contains(Constantes.SALDO_NO_DISPONIBLE));
    }

    @Test
    @DisplayName("Test cancelarFondo exitoso")
    void cancelarFondoExitoso() {
        usuario.getFondosSuscritos().add(Constantes.CODIGO_FONDO);

        TransaccionEntity transaccion = new TransaccionEntity();
        transaccion.setMonto(500.0);
        transaccion.setTipo(Constantes.APERTURA);

        when(usuarioRepository.findById(Constantes.CODIGO_USUARIO_PRUEBA)).thenReturn(Optional.of(usuario));
        when(fondoRepository.findById(Constantes.CODIGO_FONDO)).thenReturn(Optional.of(fondo));
        when(transaccionRepository.findByUsuarioIdAndFondoId(Constantes.CODIGO_USUARIO_PRUEBA, Constantes.CODIGO_FONDO))
                .thenReturn(List.of(transaccion));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);
        when(transaccionRepository.save(any(TransaccionEntity.class))).thenReturn(new TransaccionEntity());

        RespuestaCancelacionFondo respuesta = usuarioService.cancelarFondo(Constantes.CODIGO_USUARIO_PRUEBA, Constantes.CODIGO_FONDO);

        assertNotNull(respuesta);
        assertEquals(1500.0, usuario.getSaldo());
        assertFalse(usuario.getFondosSuscritos().contains("101"));
    }
}
