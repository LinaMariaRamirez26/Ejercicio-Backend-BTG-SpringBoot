package com.ejercicio.PruebaTecnica.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar el envio de notificaciones EMAIL y SMS
 */
@Service
public class NotificacionService {

    private final JavaMailSender mailSender;

    public NotificacionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un correo electrónico a un destinatario específico
     * @param destinatario
     * @param asunto
     * @param mensaje
     */
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        System.out.println("Envío de EMAIL...");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo: " + mensaje);
        System.out.println("Email enviado correctamente.");
    }

    /**
     * Envía un SMS a un número de teléfono específico.
     * @param telefono
     * @param mensaje
     */
    public void enviarSms(String telefono, String mensaje) {
        System.out.println("Enviando SMS a " + telefono + ": " + mensaje);
    }
}
