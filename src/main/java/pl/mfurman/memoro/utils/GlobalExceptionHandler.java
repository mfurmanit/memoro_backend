package pl.mfurman.memoro.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.mfurman.memoro.dto.ErrorResponse;

import static pl.mfurman.memoro.utils.StringConstants.GLOBAL_ERROR;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(final Exception exception) {
    log.error(exception.getMessage(), exception);
    return new ResponseEntity<>(
      new ErrorResponse(GLOBAL_ERROR), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
