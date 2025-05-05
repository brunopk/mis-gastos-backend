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

  public MainServiceImpl(
      CategorySpringDataRepository categoryRepository,
      SubcategorySpringDataRepository subcategoryRepository,
      GroupSpringDataRepository groupRepository,
      AccountSpringDataRepository accountRepository) {
    this.categoryRepository = categoryRepository;
    this.subcategoryRepository = subcategoryRepository;
    this.groupRepository = groupRepository;
    this.accountRepository = accountRepository;
  }

  @Override
  public List<CategoryDTO> getCategories() {
    return categoryRepository.findAll().stream()
        .map(
            category ->
                new CategoryDTO(
                    category.getId(),
                    category.getName(),
                    category.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }

  @Override
  public List<SubcategoryDTO> getSubcategories() {
    return subcategoryRepository.findAll().stream()
        .map(
            subcategory ->
                new SubcategoryDTO(
                    subcategory.getId(),
                    subcategory.getName(),
                    subcategory.getCategoryId(),
                    subcategory.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }

  @Override
  public List<GroupDTO> getGroups() {
    return groupRepository.findAll().stream()
        .map(
            group ->
                new GroupDTO(
                    group.getId(),
                    group.getName(),
                    group.getSubcategoryId(),
                    group.getAccounts().stream().map(Account::getId).toList()))
        .toList();
  }

  @Override
  public List<AccountDTO> getAccounts() {
    return accountRepository.findAll().stream()
        .map(account -> new AccountDTO(account.getId(), account.getName()))
        .toList();
  }
}
