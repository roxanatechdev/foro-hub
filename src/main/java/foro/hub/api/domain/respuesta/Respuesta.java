package foro.hub.api.domain.respuesta;

import foro.hub.api.domain.topico.Topico;
import foro.hub.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String mensaje;

  @Column(name = "fecha_de_creacion", nullable = false)
  private LocalDateTime fechaDeCreacion;

  @Column(name = "fecha_de_ultima_actualizacion", nullable = false)
  private LocalDateTime fechaDeUltimaActualizacion;

  private Boolean solucion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topico", nullable = false)
  private Topico topico;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "autor", nullable = false)
  private Usuario autor;

  public Respuesta(DatosRespuesta datos, Topico topico, Usuario autor) {
    this.mensaje = datos.mensaje();
    this.fechaDeCreacion = LocalDateTime.now();
    this.fechaDeUltimaActualizacion = LocalDateTime.now();
    this.solucion = false;
    this.topico = topico;
    this.autor = autor;
  }

  public void actualizarDatos(DatosRespuesta datosRespuesta){
    if (datosRespuesta.mensaje()!=null){
      this.mensaje= datosRespuesta.mensaje();
      this.fechaDeUltimaActualizacion=LocalDateTime.now();
    }
  }
}


