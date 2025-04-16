package MJ.sellingservice.exception;

import MJ.sellingservice.dto.response.ErrorResponse;
import MJ.sellingservice.exception.custom.AlreadyJoinException;
import MJ.sellingservice.exception.custom.HaveToLoginException;
import MJ.sellingservice.exception.custom.NoCorrectException;
import MJ.sellingservice.exception.custom.NoJoinUserException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandleException extends RuntimeException {
  @ExceptionHandler({
      NoSuchElementException.class,
      NoCorrectException.class,
      AlreadyJoinException.class,
      NoJoinUserException.class
  })
  public ResponseEntity<ErrorResponse> handleCommonBadRequest(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(LocalDateTime.now(), e.getMessage()));
  }

  @ExceptionHandler(HaveToLoginException.class)
  public ResponseEntity<ErrorResponse> handleLoginException(HaveToLoginException e){
    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
        .body(new ErrorResponse(LocalDateTime.now(), e.getMessage()));
  }
}
