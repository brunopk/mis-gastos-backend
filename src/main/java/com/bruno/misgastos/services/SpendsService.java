package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.SpendDTO;
import java.util.List;

public interface SpendsService {
  List<SpendDTO> getSpends();

  SpendDTO createSpend(SpendDTO spend);
}
