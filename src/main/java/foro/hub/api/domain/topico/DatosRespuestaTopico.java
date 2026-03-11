package foro.hub.api.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
    Long id,
    String titulo,
    String mensaje,
    LocalDateTime fechaDeCreacion,
    LocalDateTime fechaDeUltimaActualizacion,
    String autor,
    String curso
) {
}
