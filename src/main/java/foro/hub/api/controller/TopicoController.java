package foro.hub.api.controller;

import foro.hub.api.domain.respuesta.DatosListadoMisRespuestas;
import foro.hub.api.domain.respuesta.Respuesta;
import foro.hub.api.domain.respuesta.RespuestaRepository;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topicos", description = "Operaciones relacionadas con la gestión de topicos")
public class TopicoController {

  @Autowired
  private TopicoRepository topicoRepository;

  @Autowired
  private RespuestaRepository respuestaRepository;

  @PostMapping
  @Operation(
      summary = "Registrar un nuevo topico",
      description = "Permite registrar un topico en la base de datos. Es necesario proporcionar el titulo, mensaje(problema) y el curso."
  )
  public ResponseEntity<DatosMostrarRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico, UriComponentsBuilder uriComponentsBuilder, @AuthenticationPrincipal Usuario usuarioAutenticado) {
    Topico topico = topicoRepository.save(new Topico(datosRegistroTopico,usuarioAutenticado));
    DatosMostrarRespuestaTopico datosRespuestaRegistroTopico=new DatosMostrarRespuestaTopico(
        topico.getId(), topico.getTitulo(), topico.getMensaje(),topico.getFechaDeCreacion(),topico.getAutor().getUsername() ,topico.getCurso());
    URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
    return ResponseEntity.created(url).body(datosRespuestaRegistroTopico);
  }

  @GetMapping
  @Operation(
      summary = "Listar todos los Topicos",
      description = "Permite mostrar la lista de todos los topicos."
  )
  public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(@PageableDefault(size = 10, sort = "fechaDeCreacion", direction = Sort.Direction.ASC) Pageable paginacion) {
      return ResponseEntity.ok(topicoRepository.findAll(paginacion)
          .map(DatosListadoTopico::new));
  }

  @PutMapping("/{id}")
  @Transactional
  @Operation(
      summary = "Actualizar Topico",
      description = "Permite actualizar los datos de un topico del usuario. Se puede actualizar el titulo, el mensaje o el curso. Es necesario el ID del topico"
  )
  public ResponseEntity actualizarTopico(
      @PathVariable Long id,
      @RequestBody @Valid DatosActualizarTopico datosActualizarTopico,
      @AuthenticationPrincipal Usuario usuarioAutenticado) {
    Optional<Topico> topicoOpcional = topicoRepository.findById(id);

    if (topicoOpcional.isPresent()) {
      Topico topico = topicoOpcional.get();
      if (!topico.getAutor().getId().equals(usuarioAutenticado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para actualizar este tópico.");
      }
      topico.actualizarDatos(datosActualizarTopico);
      return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(),topico.getTitulo(),topico.getMensaje(),
          topico.getFechaDeCreacion(),topico.getFechaDeUltimaActualizacion(),topico.getAutor().getUsername(),
          topico.getCurso())); // 200 OK
    } else {
      return ResponseEntity.notFound().build(); // 404 Not Found
    }
  }
  @Operation(
      summary = "Eliminar un topico",
      description = "Permite eliminar un topico del usuario de la base de datos. Es necesario proporcionar el ID del topico."
  )
  @DeleteMapping("/{id}")
  public ResponseEntity eliminar(@PathVariable Long id,@AuthenticationPrincipal Usuario usuarioAutenticado ){
    Optional<Topico> topicoOpcional = topicoRepository.findById(id);
    if (topicoOpcional.isPresent()) {
      Topico topico = topicoOpcional.get();
      if (!topico.getAutor().getId().equals(usuarioAutenticado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para eliminar este tópico.");
      }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();//204
      } else {
        return ResponseEntity.notFound().build();
      }
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Obtener Topico por ID",
      description = "Permite mostrar los datos de un topico especifico. Es necesario proporcionar el ID del topico."
  )
  public ResponseEntity<DatosTopicoConRespuestas> retornaDatos(
      @PathVariable Long id,
      @PageableDefault(size = 10) Pageable paginacion) {

    Topico topico = topicoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

    DatosRespuestaTopico datosTopico = new DatosRespuestaTopico(
        topico.getId(),
        topico.getTitulo(),
        topico.getMensaje(),
        topico.getFechaDeCreacion(),
        topico.getFechaDeUltimaActualizacion(),
        topico.getAutor().getUsername(),
        topico.getCurso());

    Page<Respuesta> respuestas = respuestaRepository.respuestasPorTopico(id, paginacion);
    Page<DatosListadoMisRespuestas> respuestaPaginada = respuestas.map(DatosListadoMisRespuestas::new);

    DatosTopicoConRespuestas datosCompletos = new DatosTopicoConRespuestas(datosTopico, respuestaPaginada);

    return ResponseEntity.ok(datosCompletos);
  }


}
