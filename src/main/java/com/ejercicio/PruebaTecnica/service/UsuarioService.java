package com.ejercicio.PruebaTecnica.service;

import com.ejercicio.PruebaTecnica.dto.RespuestaCancelacionFondo;
import com.ejercicio.PruebaTecnica.jpa.entity.FondoEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.TransaccionEntity;
import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import com.ejercicio.PruebaTecnica.jpa.repository.FondoRepository;
import com.ejercicio.PruebaTecnica.jpa.repository.TransaccionRepository;
import com.ejercicio.PruebaTecnica.jpa.repository.UsuarioRepository;
import com.ejercicio.PruebaTecnica.utilies.Constantes;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio que permite todas las operaciones para la suscripcion, cancelacion y ver historial de transacciones a los fondos
 */
@Service
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
    public UsuarioEntity suscribirAFondo(String usuarioId, String fondoId, Double monto) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException(Constantes.USUARIO_NO_ENCONTRADO));

        FondoEntity fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new RuntimeException(Constantes.FONDO_NO_ENCONTRADO));

        if (monto < fondo.getMontoMinimo()) {
            throw new RuntimeException(String.format(Constantes.ERROR_MONTO_MINIMO, fondo.getNombre(), fondo.getMontoMinimo()));
        }


        if (usuario.getSaldo() < monto) {
            throw new RuntimeException(String.format(Constantes.SALDO_NO_DISPONIBLE, fondo.getNombre()));
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
                .orElseThrow(() -> new RuntimeException(Constantes.USUARIO_NO_ENCONTRADO));

        FondoEntity fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new RuntimeException(Constantes.FONDO_NO_ENCONTRADO));

        System.out.println("Fondos suscritos: " + usuario.getFondosSuscritos());
        System.out.println("FondoId a cancelar: " + fondoId);

        // Verificar si está suscrito por ID (no por nombre)
        boolean suscrito = usuario.getFondosSuscritos().contains(fondoId);

        if (!suscrito) {
            throw new RuntimeException(String.format(Constantes.USUARIO_NO_SUSCRITO,fondo.getNombre()));
        }

        // Calcular el monto invertido
        Double montoInvertido = transaccionRepository.findByUsuarioIdAndFondoId(usuarioId, fondoId).stream()
                .mapToDouble(t -> t.getTipo().equals(Constantes.APERTURA) ? t.getMonto() : -t.getMonto())
                .sum();

        if (montoInvertido <= 0) {
            throw new RuntimeException(String.format(Constantes.ERROR_SIN_SALDO_INVERTIDO, fondo.getNombre()));
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
    public List<TransaccionEntity> obtenerHistorialTransacciones(String usuarioId) {
        return transaccionRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }
}
