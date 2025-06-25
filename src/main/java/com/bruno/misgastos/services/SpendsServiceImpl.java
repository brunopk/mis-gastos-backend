package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.SpendDto;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.utils.DtoMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpendsServiceImpl implements SpendsService {

  private final SpendSpringDataRepository spendRepository;

  public SpendsServiceImpl(SpendSpringDataRepository spendRepository) {
    this.spendRepository = spendRepository;
  }

  @Override
  public List<SpendDto> getSpends() {
    return spendRepository.findAll().stream().map(DtoMapper::mapSpendToSpendDTO).toList();
  }

  @Override
  public SpendDto createSpend(SpendDto spend) {
    Spend newSpend =
        spendRepository.save(
            new Spend(
                spend.date(),
                spend.categoryId(),
                spend.subcategoryId(),
                spend.groupId(),
                spend.accountId(),
                spend.description(),
                spend.value()));
    return DtoMapper.mapSpendToSpendDTO(newSpend);
  }
}
