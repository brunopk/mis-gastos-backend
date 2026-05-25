package com.bruno.misgastos.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Income {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Integer id;

  @Column(nullable = false)
  private final OffsetDateTime date;

  @Column(name = "income_type_id", nullable = false)
  private final Integer incomeTypeId;

  @Column(name = "account_id", nullable = false)
  private final Integer accountId;

  @OneToOne
  @JoinColumn(name = "spend_id")
  private final Spend spend;

  private final String description;

  @Column(nullable = false)
  private final Double value;

  public Income(
      OffsetDateTime date,
      Integer incomeTypeId,
      Integer accountId,
      Spend spend,
      String description,
      double value) {
    this.id = null;
    this.date = date;
    this.incomeTypeId = incomeTypeId;
    this.accountId = accountId;
    this.spend = spend;
    this.description = description;
    this.value = value;
  }

  Income() {
    this.id = null;
    this.date = null;
    this.incomeTypeId = null;
    this.accountId = null;
    this.spend = null;
    this.description = null;
    this.value = null;
  }

  public Integer getId() {
    return this.id;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public Integer getIncomeTypeId() {
    return incomeTypeId;
  }

  public Spend getSpend() {
    return spend;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public String getDescription() {
    return description;
  }

  public Double getValue() {
    return value;
  }
}
