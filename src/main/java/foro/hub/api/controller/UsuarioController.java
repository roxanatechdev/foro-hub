package foro.hub.api.controller;

import foro.hub.api.domain.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioController {

  @Autowired
  UsuarioRepository usuarioRepository;

  @Autowired
  private UsuarioService usuarioService;
  @Operation(
      summary = "Registrar un nuevo usuario",
      description = "Permite registrar un usuario en la base de datos. Es necesario proporcionar un nombre, correo electrónico y contraseña."
  )
  @PostMapping
  public ResponseEntity registrarUsuario(@RequestBody @Valid DatosUsuario datos, UriComponentsBuilder uriComponentsBuilder,@AuthenticationPrincipal Usuario usuarioAutenticado) {
    System.out.println("resultado de usuario autenticado: "+usuarioAutenticado);
    if (usuarioAutenticado != null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body("Acción no permitida. Ya estás autenticado");
    }
    Usuario usuario = usuarioService.registrarUsuario(datos);
    DatosRespuestaUsuario datosRespuesta= new DatosRespuestaUsuario(usuario.getId(), usuario.getNombre(),usuario.getCorreoElectronico());
    URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
    return ResponseEntity.created(url).body(datosRespuesta);
  }

  @PutMapping("/{id}")
  @Transactional
  @Operation(
      summary = "Actualizar datos de usuario",
      description = "Permite actualizar los datos de un usuario en la base de datos. Es necesario proporcionar el id del usuario.\n Por favor, vuelve a iniciar sesión para que los cambios en tus datos de usuario surtan efecto. "
  )
  public ResponseEntity actualizarUsuario(
      @PathVariable Long id,
      @RequestBody @Valid DatosActualizarUsuario datos,@AuthenticationPrincipal Usuario usuarioAutenticado) {

    Optional<Usuario> usuarioOpcional = usuarioRepository.findById(id);

    if (usuarioOpcional.isPresent()) {
      if (!id.equals(usuarioAutenticado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para modificar estos datos.");
      }
      Usuario usuario = usuarioService.actualizarUsuario(id, datos);
      DatosRespuestaUsuario datosRespuestaUsuario=new DatosRespuestaUsuario(usuario.getId(), usuario.getNombre(),usuario.getCorreoElectronico());
      return ResponseEntity.ok(datosRespuestaUsuario);//200
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @Operation(
      summary = "Eliminar un usuario",
      description = "Permite eliminar tu usuario de la base de datos. Es necesario proporcionar el ID de tu usuario."
  )
  @DeleteMapping("/{id}")
  public ResponseEntity eliminar(@PathVariable Long id,@AuthenticationPrincipal Usuario usuarioAutenticado){
    Optional<Usuario> usuario = usuarioRepository.findById(id);

    if (usuario.isPresent()) {
        if (!id.equals(usuarioAutenticado.getId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para eliminar al usuario.");
        }
      usuarioService.eliminarUsuario(id);
      return ResponseEntity.noContent().build();//204
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Obtener un usuario por ID",
      description = "Devuelve la información de un usuario basado en su identificador único (ID)."
  )
  public ResponseEntity<DatosRespuestaUsuario> retornaDatos(@PathVariable Long id) {
    Usuario usuario = usuarioRepository.getReferenceById(id);
    var datos = new DatosRespuestaUsuario(usuario.getId(), usuario.getNombre(), usuario.getCorreoElectronico());
    return ResponseEntity.ok(datos);
  }

  @GetMapping
  @Operation(
      summary = "Muestra la lista de los usuarios",
      description = "Muestra la lista de todos los usuarios registrados en la base de datos"
  )
  public ResponseEntity<Page<DatosListadoUsuario>> listarUsuarios(@PageableDefault(size = 10) Pageable paginacion) {
    return ResponseEntity.ok(usuarioRepository.findAll(paginacion)
        .map(DatosListadoUsuario::new));
  }
}
