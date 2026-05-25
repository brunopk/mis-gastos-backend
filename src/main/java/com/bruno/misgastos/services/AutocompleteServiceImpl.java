package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.AutocompleteOptionsDto;
import com.bruno.misgastos.dto.AutocompleteQueryProjectionDto;
import com.bruno.misgastos.respositories.IncomeSpringDataRepository;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import org.springframework.stereotype.Service;

@Service
public class AutocompleteServiceImpl implements AutocompleteService {

  private final SpendSpringDataRepository spendRepository;

  private final IncomeSpringDataRepository incomeRepository;

  public AutocompleteServiceImpl(
      SpendSpringDataRepository spendRepository, IncomeSpringDataRepository incomeRepository) {
    this.spendRepository = spendRepository;
    this.incomeRepository = incomeRepository;
  }

  @Override
  public AutocompleteOptionsDto getSpendDescriptionAutocompleteOptions(String query) {
    return new AutocompleteOptionsDto(
        query,
        spendRepository.getDescriptions(query).stream()
            .map(AutocompleteQueryProjectionDto::option)
            .toList());
  }

  @Override
  public AutocompleteOptionsDto getIncomeDescriptionAutocompleteOptions(String query) {
    return new AutocompleteOptionsDto(
        query,
        incomeRepository.getDescriptions(query).stream()
            .map(AutocompleteQueryProjectionDto::option)
            .toList());
  }
}
