package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubcategoryDTO(int id, String name, @JsonProperty("category_id") int categoryId) {}
