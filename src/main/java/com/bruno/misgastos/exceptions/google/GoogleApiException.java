package com.bruno.misgastos.exceptions.google;

import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class GoogleApiException extends ApiException {

  private static final String MESSAGE = "Error interacting with Google APIs or processing their result";

  public GoogleApiException(Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_API_ERROR, MESSAGE, cause);
  }

  public GoogleApiException() {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.GOOGLE_API_ERROR, MESSAGE);
  }
}
