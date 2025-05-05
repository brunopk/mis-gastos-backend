package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.GoogleTasksService;
import com.bruno.misgastos.services.MainService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  private final MainService mainService;

  private final GoogleTasksService googleTasksService;

  @Autowired
  public MainController(MainService mainService, GoogleTasksService googleTasksService) {
    this.mainService = mainService;
    this.googleTasksService = googleTasksService;
  }

  @GetMapping("/categories")
  public ResponseEntity<List<CategoryDTO>> getCategories() {
    List<CategoryDTO> categories = mainService.getCategories();
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @GetMapping("/subcategories")
  public ResponseEntity<List<SubcategoryDTO>> getSubcategories() {
    List<SubcategoryDTO> subcategories = mainService.getSubcategories();
    return new ResponseEntity<>(subcategories, HttpStatus.OK);
  }

  @GetMapping("/groups")
  public ResponseEntity<List<GroupDTO>> getGroups() {
    List<GroupDTO> groups = mainService.getGroups();
    return new ResponseEntity<>(groups, HttpStatus.OK);
  }

  @GetMapping("/accounts")
  public ResponseEntity<List<AccountDTO>> getAccounts() {
    List<AccountDTO> groups = mainService.getAccounts();
    return new ResponseEntity<>(groups, HttpStatus.OK);
  }

  @GetMapping("/income-types")
  public ResponseEntity<List<IncomeTypeDTO>> getIncomeTypes() {
    List<IncomeTypeDTO> incomeTypes = mainService.getIncomeTypes();
    return new ResponseEntity<>(incomeTypes, HttpStatus.OK);
  }

  @GetMapping("/test-google-task")
  public ResponseEntity<Void> testGoogleTasks() throws IOException {
    googleTasksService.test();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
