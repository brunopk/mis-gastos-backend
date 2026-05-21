package com.bruno.misgastos.dto.tasks;

import com.bruno.misgastos.entities.Task;

public record TaskContextDto(Task task, String googleTaskList) {}
