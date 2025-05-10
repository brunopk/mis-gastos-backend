package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record SpendDTO(
    int id,
    @NotNull(message = "date cannot be null") OffsetDateTime date,
    @NotNull(message = "category cannot be null") @JsonProperty("category_id") Integer categoryId,
    @JsonProperty("subcategory_id") Integer subcategoryId,
    @JsonProperty("group_id") Integer groupId,
    @NotNull(message = "account cannot be null") @JsonProperty("account_id") Integer accountId,
    String description,
    @Min(value = 1, message = "Spend value must be greater than 0") double value) {}
