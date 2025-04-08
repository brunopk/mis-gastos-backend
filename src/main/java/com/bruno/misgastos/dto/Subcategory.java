package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Subcategory(int id, String name, @JsonProperty("category_id") int categoryId) {}
