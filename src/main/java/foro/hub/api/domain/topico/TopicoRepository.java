package foro.hub.api.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico,Long> {


  boolean existsByMensajeAndIdNot(String mensaje, Long idExcluido);

  boolean existsByTituloAndIdNot(String titulo, Long idExcluido);
}
