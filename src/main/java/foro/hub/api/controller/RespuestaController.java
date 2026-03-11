package foro.hub.api.controller;

import foro.hub.api.domain.respuesta.*;
import foro.hub.api.domain.topico.*;
import foro.hub.api.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuestas", description = "Operaciones relacionadas con la gestión de respuestas a los topicos")
public class RespuestaController {

  @Autowired
  private RespuestaRepository respuestaRepository;

  @Autowired
  private TopicoRepository topicoRepository;

  @PostMapping("/{idTopico}")
  @Operation(
      summary = "Registrar una nueva respuesta a un topico ",
      description = "Permite registrar una respuesta a un topico en la base de datos. Es necesario proporcionar la respueste(mensaje)."
  )
  public ResponseEntity<DatosMostrarRespuesta> registrarRespuesta(@PathVariable Long idTopico, @RequestBody @Valid DatosRespuesta datosRespuesta, UriComponentsBuilder uriComponentsBuilder, @AuthenticationPrincipal Usuario usuarioAutenticado ) {
    Optional<Topico> topicoOptional = topicoRepository.findById(idTopico);
    if (topicoOptional.isPresent()) {
      Topico topico = topicoOptional.get();
      Respuesta respuesta = new Respuesta(datosRespuesta, topico, usuarioAutenticado);
      respuestaRepository.save(respuesta);
      DatosMostrarRespuesta datosMostrarRespuesta = new DatosMostrarRespuesta(respuesta.getId(), respuesta.getTopico().getTitulo(), respuesta.getMensaje(), respuesta.getFechaDeCreacion(), respuesta.getFechaDeUltimaActualizacion(), respuesta.getAutor().getUsername());
      URI url = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
      return ResponseEntity.created(url).body(datosMostrarRespuesta);
    }else {
      return ResponseEntity.notFound().build(); // 404 Not Found
    }
  }

  @GetMapping
  @Operation(
      summary = "Listar mis respuestas",
      description = "Permite mostrar las respuestas que el usuario a dado a diferentes topicos."
  )
  public ResponseEntity<Page<DatosListadoMisRespuestas>> listarMisRespuestas(
      @PageableDefault(size = 10, sort = "fechaDeCreacion", direction = Sort.Direction.ASC) Pageable paginacion,
      @AuthenticationPrincipal Usuario usuarioAutenticado) {
    Page<Respuesta> respuestas = respuestaRepository.respuestasPorUsuario(usuarioAutenticado.getId(), paginacion);
    Page<DatosListadoMisRespuestas> respuestaPaginada = respuestas.map(DatosListadoMisRespuestas::new);
    return ResponseEntity.ok(respuestaPaginada);
  }


  @PutMapping("/{id}")
  @Transactional
  @Operation(
      summary = "Actualiza los datos de una respuesta",
      description = "Permite modificar una respuesta determinada de un usuario en la base de datos. Es necesario proporcionar el ID de la respuesta."
  )
  public ResponseEntity actualizarRespuesta(
      @PathVariable Long id,
      @RequestBody @Valid DatosRespuesta datosRespuesta,
      @AuthenticationPrincipal Usuario usuarioAutenticado) {
    Optional<Respuesta> respuestaOpcional = respuestaRepository.findById(id);

    if (respuestaOpcional.isPresent()) {
      Respuesta respuesta = respuestaOpcional.get();
      if (!respuesta.getAutor().getId().equals(usuarioAutenticado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para actualizar esta repuesta.");
      }
      respuesta.actualizarDatos(datosRespuesta);
      return ResponseEntity.ok(new DatosMostrarRespuesta(respuesta.getId(), respuesta.getTopico().getTitulo(),respuesta.getMensaje(),respuesta.getFechaDeUltimaActualizacion(),respuesta.getFechaDeUltimaActualizacion(),respuesta.getAutor().getNombre())); // 200 OK
    } else {
      return ResponseEntity.notFound().build(); // 404 Not Found
    }
  }


  @DeleteMapping("/{id}")
  @Operation(
      summary = "Eliminar una respuesta",
      description = "Permite eliminar una respuesta del usuario de la base de datos. Es necesario proporcionar el ID de la respuesta."
  )
  public ResponseEntity eliminar(@PathVariable Long id,@AuthenticationPrincipal Usuario usuarioAutenticado ){
    Optional<Respuesta> respuestaOpcional = respuestaRepository.findById(id);

    if (respuestaOpcional.isPresent()) {
      Respuesta respuesta = respuestaOpcional.get();
      if (!respuesta.getAutor().getId().equals(usuarioAutenticado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para eliminar esta repuesta.");
      }
        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();//204
      } else {
        return ResponseEntity.notFound().build();
      }
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Mostrar una respuesta por el ID",
      description = "Permite mostrar los datos de una respuesta. Se debe proporcionar el ID de la respuesta."
  )
  public ResponseEntity<DatosMostrarRespuesta> retornaDatos(@PathVariable Long id) {
    Respuesta respuesta = respuestaRepository.getReferenceById(id);
    var datosRespuesta = new DatosMostrarRespuesta(respuesta.getId(), respuesta.getTopico().getTitulo(),respuesta.getMensaje(),respuesta.getFechaDeUltimaActualizacion(),respuesta.getFechaDeUltimaActualizacion(),respuesta.getAutor().getNombre());
    return ResponseEntity.ok(datosRespuesta);
  }

}
