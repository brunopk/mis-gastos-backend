package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.IncomeDTO;
import java.util.List;

public interface IncomesService {
  List<IncomeDTO> getIncomes();

  IncomeDTO createIncome(IncomeDTO income);
}
