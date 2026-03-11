package foro.hub.api.domain.usuario;

import foro.hub.api.domain.respuesta.Respuesta;
import foro.hub.api.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  @Column(name = "correo_electronico", nullable = false, unique = true)
  private String correoElectronico;

  private String clave;

  @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Topico> topicos = new ArrayList<>();

  @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Respuesta> respuestas = new ArrayList<>();


  public Usuario(String nombre, String correoElectronico, String clave) {
    this.nombre = nombre;
    this.clave = clave;
    this.correoElectronico = correoElectronico;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return clave;
  }

  @Override
  public String getUsername() {
    return nombre;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}