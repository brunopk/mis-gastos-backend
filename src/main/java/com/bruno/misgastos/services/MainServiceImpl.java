package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.respositories.*;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

  private final CategorySpringDataRepository categoryRepository;

  private final SubcategorySpringDataRepository subcategoryRepository;

  private final GroupSpringDataRepository groupRepository;

  private final AccountSpringDataRepository accountRepository;

  private final SpendSpringDataRepository spendRepository;

  public MainServiceImpl(
      CategorySpringDataRepository categoryRepository,
      SubcategorySpringDataRepository subcategoryRepository,
      GroupSpringDataRepository groupRepository,
      AccountSpringDataRepository accountRepository,
      SpendSpringDataRepository spendRepository) {
    this.categoryRepository = categoryRepository;
    this.subcategoryRepository = subcategoryRepository;
    this.groupRepository = groupRepository;
    this.accountRepository = accountRepository;
    this.spendRepository = spendRepository;
  }

  @Override
  public List<CategoryDTO> getCategories() {
    return categoryRepository.findAll().stream()
        .map(category -> new CategoryDTO(category.getId(), category.getName()))
        .toList();
  }

  @Override
  public List<SubcategoryDTO> getSubcategories() {
    return subcategoryRepository.findAll().stream()
        .map(
            subcategory ->
                new SubcategoryDTO(
                    subcategory.getId(), subcategory.getName(), subcategory.getCategoryId()))
        .toList();
  }

  @Override
  public List<GroupDTO> getGroups() {
    return groupRepository.findAll().stream()
        .map(group -> new GroupDTO(group.getId(), group.getName(), group.getSubcategoryId()))
        .toList();
  }

  @Override
  public List<AccountDTO> getAccounts() {
    return accountRepository.findAll().stream()
        .map(account -> new AccountDTO(account.getId(), account.getName()))
        .toList();
  }

  @Override
  public List<SpendDTO> getSpends() {
    return spendRepository.findAll().stream()
        .map(
            spend ->
                new SpendDTO(
                    spend.getId(),
                    spend.getDate(),
                    spend.getCategoryId(),
                    spend.getSubcategoryId(),
                    spend.getSubcategoryId(),
                    spend.getGroupId(),
                    spend.getDescription(),
                    spend.getValue()))
        .toList();
  }
}
