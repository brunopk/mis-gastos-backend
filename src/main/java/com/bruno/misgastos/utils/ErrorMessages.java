package com.bruno.misgastos.utils;

public interface ErrorMessages {
  String GENERIC_ERROR = "Internal server error";

  String GENERIC_GOOGLE_AUTH_ERROR = "Error obtaining or saving Google tokens";

  // TODO: rename to NO_VALID_GOOGLE_TOKEN_FOUND

  String NO_VALID_TOKEN_FOUND = "No valid Google token found in database";

}