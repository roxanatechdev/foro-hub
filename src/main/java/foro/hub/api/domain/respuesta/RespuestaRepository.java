package foro.hub.api.domain.respuesta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RespuestaRepository extends JpaRepository<Respuesta,Long> {
  @Query("SELECT r FROM Respuesta r WHERE r.autor.id = :id")
  Page<Respuesta> respuestasPorUsuario(@Param("id") Long id, Pageable pageable);

  @Query("SELECT r FROM Respuesta r WHERE r.topico.id = :id")
  Page<Respuesta> respuestasPorTopico(Long id, Pageable paginacion);
}
