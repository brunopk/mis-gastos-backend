package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.SpendsService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: create an endpoint to return tasks, task JSON should contain associated task config and spend (if there's an associated spend) information

// TODO: when creating a spend, task should be checked in order to find the corresponding task, if it exists (with the same category, subcategory, and group) or return 409 if more than one tasks exist

@RestController
@RequestMapping("/spends")
public class SpendController {

  private final SpendsService spendsService;

  @Autowired
  public SpendController(SpendsService spendsService) {
    this.spendsService = spendsService;
  }

  @GetMapping
  public ResponseEntity<List<SpendDto>> getSpends() {
    List<SpendDto> spends = spendsService.getSpends();
    return new ResponseEntity<>(spends, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<SpendDto> createSpend(@Valid @RequestBody SpendDto spend) {
    SpendDto newSpend = spendsService.createSpend(spend);
    return new ResponseEntity<>(newSpend, HttpStatus.CREATED);
  }
}
