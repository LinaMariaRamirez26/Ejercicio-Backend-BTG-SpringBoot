package com.ejercicio.PruebaTecnica.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Entidad que representa la coleccion usuarios
 */
@Getter
@Setter
@Document("usuarios")
public class UsuarioEntity {

    @Id
    private String id;
    private String nombre;
    private Double saldo;
    private List<String> fondosSuscritos;
    private String email;
    private String telefono;
    private String preferencia;
}
