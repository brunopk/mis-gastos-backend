package com.bruno.misgastos.exceptions;

import com.bruno.misgastos.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

  public ResourceNotFoundException(Class<?> entityClass) {
    super(
      HttpStatus.NOT_FOUND,
      ErrorCode.RESOURCE_NOT_FOUND,
      String.format("Resource %s not found", entityClass.getName()));
  }
}