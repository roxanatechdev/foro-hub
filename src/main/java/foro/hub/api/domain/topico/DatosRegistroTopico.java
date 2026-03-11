package foro.hub.api.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatosRegistroTopico(
    @NotBlank
    String titulo,
    @NotBlank
    String mensaje,
    @NotBlank
    String curso
) {
}
