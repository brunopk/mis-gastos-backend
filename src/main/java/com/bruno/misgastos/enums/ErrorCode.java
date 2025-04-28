package com.bruno.misgastos.enums;

public enum ErrorCode {
  RESOURCE_ALREADY_EXIST, // Specific bad request case
  RESOURCE_NOT_FOUND,
  METHOD_ARGUMENT_NOT_VALIDATED, // Specific bad request case
  REST_CLIENT_EXCEPTION,
  INTERNAL_SERVER_ERROR,
  BAD_REQUEST
}