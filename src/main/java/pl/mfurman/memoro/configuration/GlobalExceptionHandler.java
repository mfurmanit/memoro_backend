package pl.mfurman.memoro.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.mfurman.memoro.dto.ErrorResponse;
import pl.mfurman.memoro.utils.exceptions.ApiException;

import static pl.mfurman.memoro.utils.StringConstants.ACCESS_DENIED_ERROR;
import static pl.mfurman.memoro.utils.StringConstants.GLOBAL_ERROR;
import static pl.mfurman.memoro.utils.StringConstants.WRONG_DATA;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException exception,
                                                                @NonNull final HttpHeaders headers,
                                                                @NonNull final HttpStatusCode status,
                                                                @NonNull final WebRequest request) {
    log.error(exception.getMessage(), exception);
    return new ResponseEntity<>(new ErrorResponse(WRONG_DATA), new HttpHeaders(), status);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Object> handleApiException(final ApiException exception) {
    log.error(exception.getMessage(), exception);
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
    log.error(exception.getMessage(), exception);
    return new ResponseEntity<>(new ErrorResponse(GLOBAL_ERROR), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(final Exception exception) {
    log.error(exception.getMessage(), exception);
    return new ResponseEntity<>(new ErrorResponse(ACCESS_DENIED_ERROR), new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(final Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(new ErrorResponse(GLOBAL_ERROR), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }
}
