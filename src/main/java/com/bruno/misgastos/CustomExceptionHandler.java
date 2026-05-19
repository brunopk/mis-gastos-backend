package com.bruno.misgastos;

import com.bruno.misgastos.dto.ErrorDto;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.exceptions.RestClientException;
import com.bruno.misgastos.exceptions.UnauthorizedException;
import com.bruno.misgastos.utils.ErrorMessages;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

  private static final String API_EXCEPTION_LOG_MESSAGE = "API exception";

  private static final String GENERIC_ERROR_LOG_MESSAGE = "Exception";

  private static final String MISSING_PARAMETER_MESSAGE = "Missing parameter %s";

  private static final String BODY_NOT_READABLE_MESSAGE = "Message not readable";

  private static final String REST_CLIENT_ERROR_MESSAGE = "REST client error";

  private static final String NO_RESOURCE_FOUND_EXCEPTION = "No resource found %s";

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorDto> handleApiException(ApiException ex) {
    ErrorDto body = new ErrorDto(ex.getErrorCode().name(), ex.getMessage());
    if (ex.getHttpStatus().value() >= HttpStatus.INTERNAL_SERVER_ERROR.value())
      LOGGER.error(API_EXCEPTION_LOG_MESSAGE, ex);
    else LOGGER.debug(API_EXCEPTION_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, ex.getHttpStatus());
  }

  // Internal server error

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception ex) {
    ErrorDto body =
        new ErrorDto(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorMessages.GENERIC_ERROR);
    LOGGER.error(GENERIC_ERROR_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RestClientException.class)
  public ResponseEntity<ErrorDto> handleRestClientException(RestClientException ex) {
    ErrorDto body = new ErrorDto(ErrorCode.INTERNAL_SERVER_ERROR.name(), REST_CLIENT_ERROR_MESSAGE);
    LOGGER.error(ex.getMessage(), ex);
    return new ResponseEntity<>(body, ex.getHttpStatus());
  }

  // Bad request

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    String errorMessage = String.format(MISSING_PARAMETER_MESSAGE, ex.getParameterName());
    ErrorDto body = new ErrorDto(ErrorCode.BAD_REQUEST.name(), errorMessage);
    LOGGER.debug(errorMessage, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    ErrorDto body = new ErrorDto(ErrorCode.BAD_REQUEST.name(), BODY_NOT_READABLE_MESSAGE);
    LOGGER.debug(BODY_NOT_READABLE_MESSAGE, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorDto> handleNoResourceFoundException(NoResourceFoundException ex) {
    String errorMessage = String.format(NO_RESOURCE_FOUND_EXCEPTION, ex.getResourcePath());
    ErrorDto body = new ErrorDto(ErrorCode.BAD_REQUEST.name(), errorMessage);
    LOGGER.debug(errorMessage, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    String errorMessage = extractErrorMessage(ex);
    ErrorDto body = new ErrorDto(ErrorCode.BAD_REQUEST.name(), errorMessage);
    LOGGER.debug(GENERIC_ERROR_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  // Unauthorized

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorDto> handleUnauthorizedException(UnauthorizedException ex) {
    ErrorDto body = new ErrorDto(ex.getErrorCode().name(), ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
  }

  private String extractErrorMessage(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.joining("\n"));
  }
}
