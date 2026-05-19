package com.bruno.misgastos.dto.rest.google.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record TaskListDto(
    @JsonProperty("kind") String kind,
    @JsonProperty("id") String id,
    @JsonProperty("etag") String eTag,
    @JsonProperty("title") String title,
    @JsonProperty("updated") OffsetDateTime updated,
    @JsonProperty("selfLink") String selfLink) {}
