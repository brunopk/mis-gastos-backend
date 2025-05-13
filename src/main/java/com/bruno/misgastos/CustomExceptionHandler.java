package com.bruno.misgastos;

import com.bruno.misgastos.dto.ErrorDTO;
import com.bruno.misgastos.enums.ErrorCode;
import com.bruno.misgastos.exceptions.ApiException;
import com.bruno.misgastos.utils.ErrorMessages;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

  private static final String API_EXCEPTION_LOG_MESSAGE = "API exception";

  private static final String GENERIC_ERROR_LOG_MESSAGE = "Exception";

  private static final String MISSING_PARAMETER_MESSAGE = "Missing parameter %s";

  private static final String NO_RESOURCE_FOUND_EXCEPTION = "No resource found %s";

  // Internal server error

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleException(Exception ex) {
    ErrorDTO body = new ErrorDTO(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorMessages.GENERIC_ERROR_MESSAGE);
    logger.error(GENERIC_ERROR_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorDTO> handleApiException(ApiException ex) {
    ErrorDTO body = new ErrorDTO(ex.getErrorCode().name(), ex.getMessage());
    if (ex.getHttpStatus().value() >= HttpStatus.INTERNAL_SERVER_ERROR.value())
      logger.error(API_EXCEPTION_LOG_MESSAGE, ex);
    else logger.debug(API_EXCEPTION_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, ex.getHttpStatus());
  }

  // Bad request
  
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDTO> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    String errorMessage = String.format(MISSING_PARAMETER_MESSAGE, ex.getParameterName());
    ErrorDTO body = new ErrorDTO(ErrorCode.BAD_REQUEST.name(), errorMessage);
    logger.debug(errorMessage, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorDTO> handleNoResourceFoundException(NoResourceFoundException ex) {
    String errorMessage = String.format(NO_RESOURCE_FOUND_EXCEPTION, ex.getResourcePath());
    ErrorDTO body = new ErrorDTO(ErrorCode.BAD_REQUEST.name(), errorMessage);
    logger.debug(errorMessage, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String errorMessage = extractErrorMessage(ex);
    ErrorDTO body = new ErrorDTO(ErrorCode.BAD_REQUEST.name(), errorMessage);
    logger.debug(GENERIC_ERROR_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  private String extractErrorMessage(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
      .map(FieldError::getDefaultMessage)
      .collect(Collectors.joining("\n"));
  }
}