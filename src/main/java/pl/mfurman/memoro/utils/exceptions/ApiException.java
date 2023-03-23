package pl.mfurman.memoro.utils.exceptions;

public class ApiException extends RuntimeException {

  public ApiException(final String message) {
    super(message);
  }

  public ApiException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
