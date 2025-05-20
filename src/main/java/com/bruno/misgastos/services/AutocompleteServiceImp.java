package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.AutocompleteOptionsDTO;
import com.bruno.misgastos.dto.AutocompleteQueryProjectionDTO;
import com.bruno.misgastos.respositories.IncomeSpringDataRepository;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import org.springframework.stereotype.Service;

// TODO: pluralize repository names

// TODO: rename this file to AutocompleteServiceImpl

@Service
public class AutocompleteServiceImp implements AutocompleteService {

  private final SpendSpringDataRepository spendsRepository;

  private final IncomeSpringDataRepository incomeRepository;

  public AutocompleteServiceImp(SpendSpringDataRepository spendRepository, IncomeSpringDataRepository incomeRepository) {
    this.spendsRepository = spendRepository;
    this.incomeRepository = incomeRepository;
  }

  @Override
  public AutocompleteOptionsDTO getSpendDescriptionAutocompleteOptions(String query) {
    return new AutocompleteOptionsDTO(
        query,
        spendsRepository.getDescriptions(query).stream()
            .map(AutocompleteQueryProjectionDTO::option)
            .toList());
  }

  @Override
  public AutocompleteOptionsDTO getIncomeDescriptionAutocompleteOptions(String query) {
    return new AutocompleteOptionsDTO(
      query,
      incomeRepository.getDescriptions(query).stream()
        .map(AutocompleteQueryProjectionDTO::option)
        .toList());
  }
}
