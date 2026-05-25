package com.bruno.misgastos.dto.rest.google.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListDto<T>(
    @JsonProperty("kind") String kind,
    @JsonProperty("etag") String eTag,
    @JsonProperty("nextPageToken") String nextPageToken,
    @JsonProperty("items") List<T> items) {}
