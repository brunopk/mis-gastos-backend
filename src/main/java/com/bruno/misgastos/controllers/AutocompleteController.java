package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.AutocompleteOptionsDto;
import com.bruno.misgastos.services.AutocompleteService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autocomplete")
public class AutocompleteController {

  private final AutocompleteService autocompleteService;

  public AutocompleteController(AutocompleteService autocompleteService) {
    this.autocompleteService = autocompleteService;
  }

  @GetMapping("/spends/description")
  public ResponseEntity<AutocompleteOptionsDto> getSpendDescriptionAutocompleteOptions(
      @NotNull @RequestParam String query) {
    AutocompleteOptionsDto result =
        autocompleteService.getSpendDescriptionAutocompleteOptions(query);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/incomes/description")
  public ResponseEntity<AutocompleteOptionsDto> getIncomeDescriptionAutocompleteOptions(
      @RequestParam String query) {
    AutocompleteOptionsDto result =
        autocompleteService.getIncomeDescriptionAutocompleteOptions(query);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
