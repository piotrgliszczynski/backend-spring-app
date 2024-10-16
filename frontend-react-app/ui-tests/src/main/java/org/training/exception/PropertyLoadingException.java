package org.training.exception;

public class PropertyLoadingException extends RuntimeException {
  public PropertyLoadingException(String message) {
    super(message);
  }

  public PropertyLoadingException(String message, Throwable cause) {
    super(message, cause);
  }
}
