package com.bruno.misgastos.dto.rest.google.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * For more information refer to <a
 * href="https://developers.google.com/workspace/tasks/reference/rest/v1/tasks#resource:-task">
 * Resource: Task</a> from the <a
 * href="https://developers.google.com/workspace/tasks/reference/rest">
 * Google Tasks v1 REST API</a>
 */
@JsonDeserialize(builder = TaskDto.Builder.class)
public class TaskDto {

  @JsonProperty("kind")
  private final String kind;

  @JsonProperty("id")
  private final String id;

  @JsonProperty("etag")
  private final String eTag;

  @JsonProperty("title")
  private final String title;

  @JsonProperty("updated")
  private final OffsetDateTime updated;

  @JsonProperty("selfLink")
  private final String selfLink;

  @JsonProperty("parent")
  private final String parent;

  @JsonProperty("position")
  private final String position;

  @JsonProperty("notes")
  private final String notes;

  @JsonProperty("status")
  private final String status;

  @JsonProperty("due")
  private final OffsetDateTime due;

  @JsonProperty("completed")
  private final String completed;

  @JsonProperty("deleted")
  private final boolean deleted;

  @JsonProperty("links")
  private final List<TaskLinkDto> links;

  @JsonProperty("webViewLink")
  private final String webViewLink;

  private TaskDto(Builder builder) {
    this.kind = builder.kind;
    this.id = builder.id;
    this.eTag = builder.eTag;
    this.title = builder.title;
    this.updated = builder.updated;
    this.selfLink = builder.selfLink;
    this.parent = builder.parent;
    this.position = builder.position;
    this.notes = builder.notes;
    this.status = builder.status;
    this.due = builder.due;
    this.completed = builder.completed;
    this.deleted = builder.deleted;
    this.links = builder.links;
    this.webViewLink = builder.webViewLink;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getKind() {
    return kind;
  }

  public String getId() {
    return id;
  }

  public String getETag() {
    return eTag;
  }

  public String getTitle() {
    return title;
  }

  public OffsetDateTime getUpdated() {
    return updated;
  }

  public String getSelfLink() {
    return selfLink;
  }

  public String getParent() {
    return parent;
  }

  public String getPosition() {
    return position;
  }

  public String getNotes() {
    return notes;
  }

  public String getStatus() {
    return status;
  }

  public OffsetDateTime getDue() {
    return due;
  }

  public String getCompleted() {
    return completed;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public List<TaskLinkDto> getLinks() {
    return links;
  }

  public String getWebViewLink() {
    return webViewLink;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String kind;
    private String id;
    private String eTag;
    private String title;
    private OffsetDateTime updated;
    private String selfLink;
    private String parent;
    private String position;
    private String notes;
    private String status;
    private OffsetDateTime due;
    private String completed;
    private boolean deleted;
    private List<TaskLinkDto> links;
    private String webViewLink;

    public Builder kind(String kind) {
      this.kind = kind;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder eTag(String eTag) {
      this.eTag = eTag;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder updated(OffsetDateTime updated) {
      this.updated = updated;
      return this;
    }

    public Builder selfLink(String selfLink) {
      this.selfLink = selfLink;
      return this;
    }

    public Builder parent(String parent) {
      this.parent = parent;
      return this;
    }

    public Builder position(String position) {
      this.position = position;
      return this;
    }

    public Builder notes(String notes) {
      this.notes = notes;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Builder due(OffsetDateTime due) {
      this.due = due;
      return this;
    }

    public Builder completed(String completed) {
      this.completed = completed;
      return this;
    }

    public Builder deleted(boolean deleted) {
      this.deleted = deleted;
      return this;
    }

    public Builder links(List<TaskLinkDto> links) {
      this.links = links;
      return this;
    }

    public Builder webViewLink(String webViewLink) {
      this.webViewLink = webViewLink;
      return this;
    }

    public TaskDto build() {
      return new TaskDto(this);
    }
  }
}
