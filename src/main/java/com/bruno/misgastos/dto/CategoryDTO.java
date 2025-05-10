package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CategoryDTO(
    int id, String name, @JsonProperty("account_ids") List<Integer> accountIds) {}
