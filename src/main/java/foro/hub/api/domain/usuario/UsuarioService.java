package foro.hub.api.domain.usuario;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Usuario registrarUsuario(DatosUsuario datos) {
    String claveEncriptada = passwordEncoder.encode(datos.clave());
    Usuario usuario = new Usuario(datos.nombre(), datos.correoElectronico(), claveEncriptada);
    return usuarioRepository.save(usuario);
  }

  public Usuario actualizarUsuario(Long id, DatosActualizarUsuario datos) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    if (datos.clave() != null) {
      usuario.setClave(passwordEncoder.encode(datos.clave()));
    }
    if (datos.nombre() != null) {
      usuario.setNombre(datos.nombre());
    }
    if (datos.correoElectronico() != null) {
      usuario.setCorreoElectronico(datos.correoElectronico());
    }
    return usuarioRepository.save(usuario);
  }

  public void eliminarUsuario(Long id) {
    usuarioRepository.deleteById(id);
  }
}
