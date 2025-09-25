package com.ejercicio.PruebaTecnica.service;

import com.ejercicio.PruebaTecnica.dto.RespuestaCancelacionFondo;
import com.ejercicio.PruebaTecnica.exception.*;
import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.TransaccionEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import com.ejercicio.PruebaTecnica.jpa.repository.FondoRepository;
import com.ejercicio.PruebaTecnica.jpa.repository.TransaccionRepository;
import com.ejercicio.PruebaTecnica.jpa.repository.UsuarioRepository;
import com.ejercicio.PruebaTecnica.utilies.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio que permite todas las operaciones para la
 * suscripcion, cancelacion y ver historial de transacciones de los fondos
 */
@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TransaccionRepository transaccionRepository;
    private final NotificacionService notificacionService;
    private final FondoRepository fondoRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository, TransaccionRepository transaccionRepository,
            FondoRepository fondoRepository, NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.transaccionRepository = transaccionRepository;
        this.fondoRepository= fondoRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Servicio utilizado para obtener los ususarios
     * @param id
     * @return
     */
    public Optional<UsuarioEntity> obtenerUsuario(String id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Servicio que permite la suscripcion a un nuevo fondo
     * @param usuarioId
     * @param fondoId
     * @param monto
     * @return
     */
    @Transactional
    public UsuarioEntity suscribirAFondo(String usuarioId, String fondoId, Double monto) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(Constantes.USUARIO_NO_ENCONTRADO));

        FondoEntity fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new FondoNoEncontradoException(Constantes.FONDO_NO_ENCONTRADO));

        if (monto < fondo.getMontoMinimo()) {
            throw new MontoMinimoException(String.format(Constantes.ERROR_MONTO_MINIMO, fondo.getNombre(), fondo.getMontoMinimo()));
        }


        if (usuario.getSaldo() < monto) {
            throw new SaldoInsuficienteException(String.format(Constantes.SALDO_NO_DISPONIBLE, fondo.getNombre()));
        }

        // descontar saldo
        usuario.setSaldo(usuario.getSaldo() - monto);

        // agregar fondo a la lista (guarda id y monto invertido)
        usuario.getFondosSuscritos().add(fondoId);

        // guardar usuario actualizado
        usuarioRepository.save(usuario);

        // crear y guardar transacción
        TransaccionEntity transaccion = new TransaccionEntity();
        transaccion.setId(UUID.randomUUID().toString());
        transaccion.setUsuarioId(usuarioId);
        transaccion.setFondoId(fondoId);
        transaccion.setMonto(monto);
        transaccion.setTipo(Constantes.APERTURA);
        transaccion.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccion);

        // enviar notificación
        if ("Email".equalsIgnoreCase(usuario.getPreferencia())) {
            notificacionService.enviarCorreo(
                    usuario.getEmail(),
                    "Suscripción a fondo exitosa",
                    "Hola " + usuario.getNombre() +
                            ", te has suscrito al fondo " +
                            " con un monto de " + monto + " COP."
            );
        } else if ("SMS".equalsIgnoreCase(usuario.getPreferencia())) {
            notificacionService.enviarSms(
                    usuario.getTelefono(),
                    "Hola " + usuario.getNombre() +
                            ", te suscribiste al fondo " +
                            " con " + monto + " COP."
            );
        }
        return usuario;
    }

    /**
     * Servicio utilizado para cancelar un fondo
     * @param usuarioId
     * @param fondoId
     * @return
     */
    public RespuestaCancelacionFondo cancelarFondo(String usuarioId, String fondoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(Constantes.USUARIO_NO_ENCONTRADO));

        FondoEntity fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new FondoNoEncontradoException(Constantes.FONDO_NO_ENCONTRADO));

        log.info("Fondos suscritos: " + usuario.getFondosSuscritos());
        log.info("Id Fondo a cancelar: " + fondoId);

        // Verificar si está suscrito por ID (no por nombre)
        boolean suscrito = usuario.getFondosSuscritos().contains(fondoId);

        if (!suscrito) {
            throw new UsuarioNoSuscritoException(String.format(Constantes.USUARIO_NO_SUSCRITO,fondo.getNombre()));
        }

        // Calcular el monto invertido
        Double montoInvertido = transaccionRepository.findByUsuarioIdAndFondoId(usuarioId, fondoId).stream()
                .mapToDouble(t -> t.getTipo().equals(Constantes.APERTURA) ? t.getMonto() : -t.getMonto())
                .sum();

        if (montoInvertido <= 0) {
            throw new SinSaldoInvertidoException(String.format(Constantes.ERROR_SIN_SALDO_INVERTIDO, fondo.getNombre()));
        }

        // Actualizar saldo y eliminar fondo
        usuario.setSaldo(usuario.getSaldo() + montoInvertido);
        usuario.getFondosSuscritos().remove(fondoId);
        usuarioRepository.save(usuario);

        // Registrar transacción
        TransaccionEntity transaccion = new TransaccionEntity();
        transaccion.setId(UUID.randomUUID().toString());
        transaccion.setUsuarioId(usuarioId);
        transaccion.setFondoId(fondoId);
        transaccion.setMonto(montoInvertido);
        transaccion.setTipo(Constantes.CANCELACION);
        transaccion.setFecha(LocalDateTime.now());

        transaccionRepository.save(transaccion);

        return new RespuestaCancelacionFondo(usuario, fondo.getNombre());
    }

    /**
     * Servicio utilizado para obtener el historial de transacciones realizadas
     * @param usuarioId
     * @return
     */
    public Page<TransaccionEntity> obtenerHistorialTransacciones(String usuarioId, Pageable pageable) {
        return transaccionRepository.findByUsuarioIdOrderByFechaDesc(usuarioId, pageable);
    }

}
