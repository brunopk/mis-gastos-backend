package com.bruno.misgastos.dto.google;

// TODO: test if task is created with the correct due date (including timezone)

import java.time.OffsetDateTime;

public record Task(OffsetDateTime due, String notes, String title) {}
