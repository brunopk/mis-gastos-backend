package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.AutocompleteOptionsDTO;

public interface AutocompleteService {
  AutocompleteOptionsDTO getSpendDescriptionAutocompleteOptions(String query);

  AutocompleteOptionsDTO getIncomeDescriptionAutocompleteOptions(String query);
}
