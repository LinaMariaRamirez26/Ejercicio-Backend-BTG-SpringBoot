package com.ejercicio.PruebaTecnica.jpa.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fondos")
public class FondoEntity {

    @Id
    private String id;
    private String nombre;
    private Double montoMinimo;
    private String categoria;

 }
