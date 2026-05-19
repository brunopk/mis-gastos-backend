package com.bruno.misgastos.utils;

import com.bruno.misgastos.dto.SpendDto;
import com.bruno.misgastos.entities.Spend;

public interface DtoMapper {
  static SpendDto mapSpendToSpendDTO(Spend spend) {
    return new SpendDto(
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
