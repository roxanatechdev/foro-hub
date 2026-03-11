package foro.hub.api.domain.respuesta;

import foro.hub.api.domain.topico.Topico;

import java.time.LocalDateTime;

public record DatosListadoMisRespuestas(
    Long id,
    String topico,
    String mensaje,
    LocalDateTime fechaDeCreacion,
    LocalDateTime fechaDeUltimaActualizacion,
    Boolean solucion
) {
  public DatosListadoMisRespuestas(Respuesta respuesta){
    this(
        respuesta.getId(),
        respuesta.getTopico().getTitulo(),
        respuesta.getMensaje(),
        respuesta.getFechaDeCreacion(),
        respuesta.getFechaDeUltimaActualizacion(),
        respuesta.getSolucion());
  }
}
