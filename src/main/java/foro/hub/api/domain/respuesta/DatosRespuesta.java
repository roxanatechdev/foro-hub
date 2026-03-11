package foro.hub.api.domain.respuesta;

import jakarta.validation.constraints.NotBlank;

public record DatosRespuesta(
    @NotBlank
    String mensaje
) {
}
