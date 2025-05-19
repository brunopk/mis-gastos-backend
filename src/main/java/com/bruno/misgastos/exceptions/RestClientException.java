package com.bruno.misgastos.exceptions;

import com.bruno.misgastos.enums.ErrorCode;
import java.io.InputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RestClientException extends ApiException {

  private final HttpStatusCode responseStatus;

  private final String responseBody;
  
  public RestClientException(String url, HttpStatusCode responseStatus, String responseBody) {
    super(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorCode.REST_CLIENT_EXCEPTION,
        String.format("%s returned HTTP %s", url, responseStatus.value()));
    this.responseStatus = responseStatus;
    this.responseBody = responseBody;
  }

  public HttpStatusCode getResponseStatus() {
    return responseStatus;
  }

  public String getResponseBody() {
    return responseBody;
  }
}
