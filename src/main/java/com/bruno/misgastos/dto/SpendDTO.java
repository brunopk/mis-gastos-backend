package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public record SpendDTO(
    int id,
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd") Date date,
    @NotNull @JsonProperty("category_id") Integer categoryId,
    @JsonProperty("subcategory_id") Integer subcategoryId,
    @JsonProperty("group_id") Integer groupId,
    @NotNull @JsonProperty("account_id") Integer accountId,
    String description,
    @Min(value = 1, message = "Spend value must be greater than 0") double value) {}
