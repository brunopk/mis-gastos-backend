package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.MainService;
import com.bruno.misgastos.services.google.GoogleMailService;
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

  private final GoogleMailService googleMailService;

  @Autowired
  public MainController(MainService mainService, GoogleMailService googleMailService) {
    this.mainService = mainService;
    this.googleMailService = googleMailService;
  }

  @GetMapping("/categories")
  public ResponseEntity<List<CategoryDto>> getCategories() {
    List<CategoryDto> categories = mainService.getCategories();
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @GetMapping("/subcategories")
  public ResponseEntity<List<SubcategoryDto>> getSubcategories() {
    List<SubcategoryDto> subcategories = mainService.getSubcategories();
    return new ResponseEntity<>(subcategories, HttpStatus.OK);
  }

  @GetMapping("/groups")
  public ResponseEntity<List<GroupDto>> getGroups() {
    List<GroupDto> groups = mainService.getGroups();
    return new ResponseEntity<>(groups, HttpStatus.OK);
  }

  @GetMapping("/accounts")
  public ResponseEntity<List<AccountDto>> getAccounts() {
    List<AccountDto> groups = mainService.getAccounts();
    return new ResponseEntity<>(groups, HttpStatus.OK);
  }

  @GetMapping("/income-types")
  public ResponseEntity<List<IncomeTypeDto>> getIncomeTypes() {
    List<IncomeTypeDto> incomeTypes = mainService.getIncomeTypes();
    return new ResponseEntity<>(incomeTypes, HttpStatus.OK);
  }

  @GetMapping("/test-google-task")
  public ResponseEntity<Void> testGoogleTasks() throws IOException {
    googleMailService.sendMail("brunopiaggiok@gmail.com", "Test", "<h1>This is the HTML version</h1>");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
