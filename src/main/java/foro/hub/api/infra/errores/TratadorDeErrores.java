package foro.hub.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErrores {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity tratarError404(){
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity tratarError400(MethodArgumentNotValidException e){
    var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
    return ResponseEntity.badRequest().body(errores);
  }

  private record DatosErrorValidacion(String campo, String error){
    public DatosErrorValidacion(FieldError error) {
      this(error.getField(), error.getDefaultMessage());
    }
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(DataIntegrityViolationException ex) {
    return ResponseEntity.badRequest().body("No se puede guardar la información porque ya existe un registro con esos datos. Por favor, intenta con otros");
  }

}