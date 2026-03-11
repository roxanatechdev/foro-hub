package foro.hub.api.domain.topico;

import foro.hub.api.domain.respuesta.Respuesta;
import foro.hub.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Topico")
@Table(name = "topicos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Topico {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  private String titulo;
  private String mensaje;
  @Column(name = "fecha_de_creacion")
  private LocalDateTime fechaDeCreacion;
  @Column(name = "fecha_de_ultima_actualizacion")
  private LocalDateTime fechaDeUltimaActualizacion;
  private Boolean status;
  private String curso;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "autor", nullable = false)
  private Usuario autor;

  @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Respuesta> respuestas = new ArrayList<>();



  public Topico(DatosRegistroTopico datosRegistroTopico, Usuario autor) {
    this.titulo=datosRegistroTopico.titulo();
    this.mensaje=datosRegistroTopico.mensaje();
    this.fechaDeCreacion = LocalDateTime.now();
    this.fechaDeUltimaActualizacion = LocalDateTime.now();
    this.status=true;
    this.autor=autor;
    this.curso= datosRegistroTopico.curso();
  }

  public void actualizarDatos(DatosActualizarTopico datosActualizarTopico) {
    if(datosActualizarTopico.titulo()!=null) {
      this.titulo=datosActualizarTopico.titulo();
    }

    if(datosActualizarTopico.mensaje()!=null) {
      this.mensaje=datosActualizarTopico.mensaje();
    }

    if(datosActualizarTopico.curso()!=null) {
      this.curso= datosActualizarTopico.curso();
    }
    this.fechaDeUltimaActualizacion=LocalDateTime.now();
  }
}
