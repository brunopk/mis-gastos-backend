package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.rest.google.tasks.ListDto;
import com.bruno.misgastos.dto.rest.google.tasks.TaskListDto;
import com.bruno.misgastos.services.google.GoogleTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/google")
public class GoogleController {
  
  private final GoogleTasksService googleTaskService;

  @Autowired
  public GoogleController(GoogleTasksService googleTaskService) {
    this.googleTaskService = googleTaskService;
  }

  @GetMapping("/tasks/task-lists")
  public ResponseEntity<ListDto<TaskListDto>> listTasksList(Authentication authentication) {
    String principalName = authentication.getName();
    ListDto<TaskListDto> tasksLists = googleTaskService.listTaskLists(principalName);
    return new ResponseEntity<>(tasksLists, HttpStatus.OK);
  }
}
