package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.SpendsService;
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
  public ResponseEntity<List<SpendDTO>> getSpends() {
    List<SpendDTO> spends = spendsService.getSpends();
    return new ResponseEntity<>(spends, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<SpendDTO> createSpend(@RequestBody SpendDTO spend) {
    SpendDTO newSpend = spendsService.createSpend(spend);
    return new ResponseEntity<>(newSpend, HttpStatus.CREATED);
  }
}
