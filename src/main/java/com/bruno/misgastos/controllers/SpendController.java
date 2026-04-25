package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.SpendsService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
