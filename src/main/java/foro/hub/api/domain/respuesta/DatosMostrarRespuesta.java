package foro.hub.api.domain.respuesta;

import java.time.LocalDateTime;

public record DatosMostrarRespuesta(
    Long id,
    String topico,
    String mensaje,
    LocalDateTime fechaDeCreacion,
    LocalDateTime fechaDeUltimaActualizacion,
    String autor
) {
}
