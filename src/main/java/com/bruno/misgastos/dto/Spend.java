package com.bruno.misgastos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record Spend(
    int id,
    Date date,
    @JsonProperty("category_id") int categoryId,
    @JsonProperty("subcategory_id") int subcategoryId,
    @JsonProperty("group_id") int groupId,
    @JsonProperty("account_id") int accountId,
    String description,
    double value) {}
