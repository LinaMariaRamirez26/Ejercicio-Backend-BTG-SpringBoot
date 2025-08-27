package com.ejercicio.PruebaTecnica.jpa.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Document(collection = "transacciones")
public class TransaccionEntity {

    @Id
    private String id;
    private String usuarioId;
    private String fondoId;
    private Double monto;
    private String tipo; // "APERTURA" o "CANCELACION"
    private LocalDateTime fecha;
}
