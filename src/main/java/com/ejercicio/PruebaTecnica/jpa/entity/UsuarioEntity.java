package com.ejercicio.PruebaTecnica.jpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document("usuarios")
public class UsuarioEntity {

    @Id
    private String id;
    private String nombre;
    private Double saldo;
    private List<String> fondosSuscritos;
    private String email;         // correo del usuario
    private String telefono;      // número para SMS
    private String preferencia;   // "EMAIL" o "SMS"
}
