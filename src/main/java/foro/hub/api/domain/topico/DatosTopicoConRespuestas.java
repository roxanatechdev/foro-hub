package foro.hub.api.domain.topico;

import foro.hub.api.domain.respuesta.DatosListadoMisRespuestas;
import org.springframework.data.domain.Page;

public record DatosTopicoConRespuestas(
    DatosRespuestaTopico topico,
    Page<DatosListadoMisRespuestas> respuestas
) {}
