package com.bruno.misgastos.exceptions;

import com.bruno.misgastos.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private final HttpStatus httpStatus;

  private final ErrorCode errorCode;

  private final String message;

  public ApiException(HttpStatus httpStatus, ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.message = message;
  }

  public ApiException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.message = message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    return message;
  }
}