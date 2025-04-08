package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Group(int id, String name, @JsonProperty("subcategory_id") int subcategoryId) {}
