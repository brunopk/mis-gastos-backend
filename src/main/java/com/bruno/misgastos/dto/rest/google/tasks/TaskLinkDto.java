package com.bruno.misgastos.dto.rest.google.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

/**
 * For more information refer to <a
 * href="https://developers.google.com/workspace/tasks/reference/rest/v1/tasks#resource:-task">Resource:
 * Task</a> from the <a href="https://developers.google.com/workspace/tasks/reference/rest">Google
 * Tasks v1 REST API</>
 * @param type
 * @param description
 * @param link
 */
public record TaskLinkDto(
    @JsonProperty("type") String type,
    @JsonProperty("description") String description,
    @JsonProperty("link") String link) {}
