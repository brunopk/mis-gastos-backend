package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSpringDataRepository extends JpaRepository<Account, Integer> {}
