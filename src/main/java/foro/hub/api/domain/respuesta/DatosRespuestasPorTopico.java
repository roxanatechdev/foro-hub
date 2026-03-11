package foro.hub.api.domain.respuesta;

import java.time.LocalDateTime;

public record DatosRespuestasPorTopico(
    Long id,
    String mensaje,
    LocalDateTime fechaDeCreacion,
    LocalDateTime fechaDeUltimaActualizacion,
    Boolean solucion
) {
  public DatosRespuestasPorTopico(Respuesta respuesta){
    this(
        respuesta.getId(),
        respuesta.getMensaje(),
        respuesta.getFechaDeCreacion(),
        respuesta.getFechaDeUltimaActualizacion(),
        respuesta.getSolucion());
  }
}
