package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.IncomesService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incomes")
public class IncomesController {

  private final IncomesService incomesService;

  @Autowired
  public IncomesController(IncomesService incomesService) {
    this.incomesService = incomesService;
  }

  @GetMapping
  public ResponseEntity<List<IncomeDTO>> getIncomes() {
    List<IncomeDTO> incomes = incomesService.getIncomes();
    return new ResponseEntity<>(incomes, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<IncomeDTO> createIncome(@Valid @RequestBody IncomeDTO income) {
    IncomeDTO newIncome = incomesService.createIncome(income);
    return new ResponseEntity<>(newIncome, HttpStatus.CREATED);
  }
}
