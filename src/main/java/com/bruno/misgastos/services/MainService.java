package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.*;
import java.util.List;

public interface MainService {

  List<CategoryDto> getCategories();

  List<SubcategoryDto> getSubcategories();

  List<GroupDto> getGroups();

  List<AccountDto> getAccounts();

  List<IncomeTypeDto> getIncomeTypes();
}
