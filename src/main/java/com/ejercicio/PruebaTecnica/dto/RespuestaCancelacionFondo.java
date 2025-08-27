package com.ejercicio.PruebaTecnica.dto;

import com.ejercicio.PruebaTecnica.jpa.entity.UsuarioEntity;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RespuestaCancelacionFondo {
    private UsuarioEntity usuario;
    private String fondoCancelado;

    public RespuestaCancelacionFondo(UsuarioEntity usuario, String fondoCancelado) {
        this.usuario = usuario;
        this.fondoCancelado = fondoCancelado;
    }
}
