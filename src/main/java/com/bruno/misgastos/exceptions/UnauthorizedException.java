package com.bruno.misgastos.exceptions;

import com.bruno.misgastos.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
  public UnauthorizedException(ErrorCode errorCode) {
    super(HttpStatus.UNAUTHORIZED, errorCode, HttpStatus.UNAUTHORIZED.name().toLowerCase());
  }

  public UnauthorizedException(ErrorCode errorCode, String message) {
    super(HttpStatus.UNAUTHORIZED, errorCode, message);
  }
}
