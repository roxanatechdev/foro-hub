package foro.hub.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosActualizarUsuario(
    String nombre,
    @Email
    String correoElectronico,
    String clave
) {
}
