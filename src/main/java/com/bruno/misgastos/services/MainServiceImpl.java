package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.entities.Account;
import com.bruno.misgastos.respositories.*;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

  private final CategorySpringDataRepository categoryRepository;

  private final SubcategorySpringDataRepository subcategoryRepository;

  private final GroupSpringDataRepository groupRepository;

  private final AccountSpringDataRepository accountRepository;

  private final IncomeTypeSpringDataRepository incomeTypeRepository;

  public MainServiceImpl(
      CategorySpringDataRepository categoryRepository,
      SubcategorySpringDataRepository subcategoryRepository,
      GroupSpringDataRepository groupRepository,
      AccountSpringDataRepository accountRepository,
      IncomeTypeSpringDataRepository incomeTypeRepository) {
    this.categoryRepository = categoryRepository;
    this.subcategoryRepository = subcategoryRepository;
    this.groupRepository = groupRepository;
    this.accountRepository = accountRepository;
    this.incomeTypeRepository = incomeTypeRepository;
  }

  @Override
  public List<CategoryDto> getCategories() {
    return categoryRepository.findAll().stream()
        .map(
            category ->
                new CategoryDto(
                    category.getId(),
                    category.getName(),
                    category.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }

  @Override
  public List<SubcategoryDto> getSubcategories() {
    return subcategoryRepository.findAll().stream()
        .map(
            subcategory ->
                new SubcategoryDto(
                    subcategory.getId(),
                    subcategory.getName(),
                    subcategory.getCategoryId(),
                    subcategory.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }

  @Override
  public List<GroupDto> getGroups() {
    return groupRepository.findAll().stream()
        .map(
            group ->
                new GroupDto(
                    group.getId(),
                    group.getName(),
                    group.getSubcategoryId(),
                    group.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }

  @Override
  public List<AccountDto> getAccounts() {
    return accountRepository.findAll().stream()
        .map(account -> new AccountDto(account.getId(), account.getName()))
        .toList();
  }

  @Override
  public List<IncomeTypeDto> getIncomeTypes() {
    return incomeTypeRepository.findAll().stream()
        .map(
            incomeType ->
                new IncomeTypeDto(
                    incomeType.getId(),
                    incomeType.getName(),
                    incomeType.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }
}
