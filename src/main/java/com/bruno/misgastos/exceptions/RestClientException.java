package com.bruno.misgastos.exceptions;

import com.bruno.misgastos.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RestClientException extends ApiException {

  public RestClientException(String url, HttpStatusCode responseStatus, String responseBody) {
    super(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorCode.REST_CLIENT_EXCEPTION,
        String.format("%s return HTTP status %s and response body :\n %s", url, responseStatus.value(), responseBody));
  }
}
