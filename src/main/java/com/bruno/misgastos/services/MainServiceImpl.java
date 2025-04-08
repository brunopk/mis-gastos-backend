package com.bruno.misgastos.services;

import com.bruno.misgastos.dao.*;
import com.bruno.misgastos.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

  private final CategoryDAO categoryDAO;

  private final SubcategoryDAO subcategoryDAO;

  private final GroupDAO groupDAO;

  private final AccountDAO accountDAO;

  private final SpendDAO spendDAO;

  public MainServiceImpl(
      CategoryDAO categoryDAO,
      SubcategoryDAO subcategoryDAO,
      GroupDAO groupDAO,
      AccountDAO accountDAO,
      SpendDAO spendDAO) {
    this.categoryDAO = categoryDAO;
    this.subcategoryDAO = subcategoryDAO;
    this.groupDAO = groupDAO;
    this.accountDAO = accountDAO;
    this.spendDAO = spendDAO;
  }

  @Override
  public List<Category> getCategories() {
    return categoryDAO.list();
  }

  @Override
  public List<Subcategory> getSubcategories() {
    return subcategoryDAO.list();
  }

  @Override
  public List<Group> getGroups() {
    return groupDAO.list();
  }

  @Override
  public List<Account> getAccounts() {
    return accountDAO.list();
  }

  @Override
  public List<Spend> getSpends() {
    return spendDAO.list();
  }
}
