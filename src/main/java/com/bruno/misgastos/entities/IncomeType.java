package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class IncomeType {

  @Id private final Integer id;

  private final String name;

  @ManyToMany
  @JoinTable(
      name = "income_type_account",
      joinColumns = @JoinColumn(name = "income_type_id"),
      inverseJoinColumns = @JoinColumn(name = "account_id"))
  private final List<Account> accounts;

  IncomeType() {
    this.id = null;
    this.name = null;
    this.accounts = null;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public List<Account> getAccounts() {
    return accounts;
  }
}
