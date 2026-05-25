package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.IncomeDto;
import java.util.List;

public interface IncomesService {
  List<IncomeDto> getIncomes();

  IncomeDto createIncome(IncomeDto income);
}
