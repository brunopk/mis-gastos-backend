package com.bruno.misgastos.utils;

import com.bruno.misgastos.dto.SpendDTO;
import com.bruno.misgastos.entities.Spend;

public interface DTOMapper {
  static SpendDTO mapSpendToSpendDTO(Spend spend) {
    return new SpendDTO(
        spend.getId(),
        spend.getDate(),
        spend.getCategoryId(),
        spend.getSubcategoryId(),
        spend.getGroupId(),
        spend.getAccountId(),
        spend.getDescription(),
        spend.getValue());
  }
}
