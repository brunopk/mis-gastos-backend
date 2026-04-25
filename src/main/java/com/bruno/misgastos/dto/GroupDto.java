package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GroupDto(
    int id,
    String name,
    @JsonProperty("subcategory_id") int subcategoryId,
    @JsonProperty("account_ids") List<Integer> accountIds) {}
