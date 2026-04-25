package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.AutocompleteOptionsDto;

public interface AutocompleteService {
  AutocompleteOptionsDto getSpendDescriptionAutocompleteOptions(String query);

  AutocompleteOptionsDto getIncomeDescriptionAutocompleteOptions(String query);
}
