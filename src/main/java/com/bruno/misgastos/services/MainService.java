package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.*;

import java.util.List;

public interface MainService {

    List<Category> getCategories();

    List<Subcategory> getSubcategories();

    List<Group> getGroups();

    List<Account> getAccounts();

    List<Spend> getSpends();
}
