package com.bruno.misgastos.dto;

import java.util.List;

public record AutocompleteOptionsDTO(String query, List<String> options) {}
