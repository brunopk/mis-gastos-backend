package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SubcategoryDTO(
    int id,
    String name,
    @JsonProperty("category_id") int categoryId,
    @JsonProperty("account_ids") List<Integer> accountIds) {}
