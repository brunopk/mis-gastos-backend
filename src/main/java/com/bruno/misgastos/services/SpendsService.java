package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.SpendDto;
import java.util.List;

public interface SpendsService {
  List<SpendDto> getSpends();

  SpendDto createSpend(SpendDto spend);
}
