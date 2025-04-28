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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

  private static final String API_EXCEPTION_LOG_MESSAGE = "API exception";

  private static final String GENERIC_ERROR_LOG_MESSAGE = "Exception";

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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String errorMessage = extractErrorMessage(ex);
    ErrorDTO body = new ErrorDTO(ErrorCode.METHOD_ARGUMENT_NOT_VALIDATED.name(), errorMessage);
    logger.debug(GENERIC_ERROR_LOG_MESSAGE, ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  private String extractErrorMessage(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
      .map(FieldError::getDefaultMessage)
      .collect(Collectors.joining("\n"));
  }
}