package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupDTO(int id, String name, @JsonProperty("subcategory_id") int subcategoryId) {}
