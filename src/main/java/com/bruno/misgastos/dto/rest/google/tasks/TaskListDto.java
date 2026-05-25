package com.bruno.misgastos.dto.rest.google.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

/**
 * For more information refer to <a
 * href="https://developers.google.com/workspace/tasks/reference/rest/v1/tasklists#resource:-tasklist">Resource:
 * TaskList</a> from the <a
 * href="https://developers.google.com/workspace/tasks/reference/rest">Google Tasks v1 REST API</>
 *
 * @param kind
 * @param id
 * @param eTag
 * @param title
 * @param updated
 * @param selfLink
 */
public record TaskListDto(
    @JsonProperty("kind") String kind,
    @JsonProperty("id") String id,
    @JsonProperty("etag") String eTag,
    @JsonProperty("title") String title,
    @JsonProperty("updated") OffsetDateTime updated,
    @JsonProperty("selfLink") String selfLink) {}
