package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"group\"")
public class Group {

  @Id
  private final Integer id;

  private final String name;

  @Column(name="category_id")
  private final Integer categoryId;

  @Column(name="subcategory_id")
  private final Integer subcategoryId;

  @ManyToMany
  @JoinTable(
    name = "group_account",
    joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "account_id"))
  private final List<Account> accounts;

  Group() {
    this.id = null;
    this.name = null;
    this.categoryId = null;
    this.subcategoryId = null;
    this.accounts = null;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public Integer getSubcategoryId() {
    return subcategoryId;
  }

  public List<Account> getAccounts() {
    return accounts;
  }
}
