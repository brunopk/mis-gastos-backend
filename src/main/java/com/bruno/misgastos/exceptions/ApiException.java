package com.bruno.misgastos.exceptions;

import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.utils.ErrorMessages;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private final HttpStatus httpStatus;

  private final ErrorCode errorCode;

  public ApiException(HttpStatus httpStatus, ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }

  public ApiException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }

  public ApiException(ErrorCode errorCode, String message) {
    super(message);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errorCode = errorCode;
  }

  public ApiException(Throwable ex) {
    super(ErrorMessages.GENERIC_ERROR, ex);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}