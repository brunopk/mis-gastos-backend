package com.bruno.misgastos.dao;

import com.bruno.misgastos.dto.Account;
import java.util.List;

// TODO: fix account id as 0 for spends

public interface AccountDAO {
    List<Account> list();
}
