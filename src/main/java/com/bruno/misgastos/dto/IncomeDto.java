package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record IncomeDto(
    int id,
    @NotNull(message = "date cannot be null") OffsetDateTime date,
    @NotNull(message = "income type cannot be null") @JsonProperty("income_type_id")
        Integer incomeTypeId,
    @NotNull(message = "account cannot be null") @JsonProperty("account_id") Integer accountId,
    SpendDto spend,
    String description,
    @Min(value = 1, message = "Spend value must be greater than 0") double value) {}
