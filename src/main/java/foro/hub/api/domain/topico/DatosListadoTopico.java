package foro.hub.api.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(
    Long id,
    String titulo,
    LocalDateTime fechaDeCreacion,
    LocalDateTime fechaDeUltimaActualizacion,
    String autor,
    String curso
) {
  public DatosListadoTopico(Topico topico){
        this(
            topico.getId(),
            topico.getTitulo(),
            topico.getFechaDeCreacion(),
            topico.getFechaDeUltimaActualizacion(),
            topico.getAutor().getUsername(),
            topico.getCurso());
  }
}
