package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

// TODO: add Date formatter to return just date (no hours, minutes etc)

public record SpendDTO(
    int id,
    Date date,
    @JsonProperty("category_id") Integer categoryId,
    @JsonProperty("subcategory_id") Integer subcategoryId,
    @JsonProperty("group_id") Integer groupId,
    @JsonProperty("account_id") Integer accountId,
    String description,
    double value) {}
