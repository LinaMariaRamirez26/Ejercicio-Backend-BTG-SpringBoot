package com.ejercicio.PruebaTecnica.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private final JavaMailSender mailSender;

    public NotificacionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        System.out.println("Envío de EMAIL...");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo: " + mensaje);
        System.out.println("Email enviado correctamente.");
    }

    public void enviarSms(String telefono, String mensaje) {
        System.out.println("Enviando SMS a " + telefono + ": " + mensaje);
    }
}
