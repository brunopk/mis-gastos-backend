package com.bruno.misgastos.dto;

import java.util.List;

public record AutocompleteOptionsDto(String query, List<String> options) {}
