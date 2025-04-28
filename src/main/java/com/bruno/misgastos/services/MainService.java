package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.*;
import java.util.List;

public interface MainService {

    List<CategoryDTO> getCategories();

    List<SubcategoryDTO> getSubcategories();

    List<GroupDTO> getGroups();

    List<AccountDTO> getAccounts();
}
