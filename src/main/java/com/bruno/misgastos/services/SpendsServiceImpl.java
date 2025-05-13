package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.SpendDTO;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.utils.DTOMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpendsServiceImpl implements SpendsService {

  private final SpendSpringDataRepository spendRepository;

  public SpendsServiceImpl(SpendSpringDataRepository spendRepository) {
    this.spendRepository = spendRepository;
  }

  @Override
  public List<SpendDTO> getSpends() {
    return spendRepository.findAll().stream().map(DTOMapper::mapSpendToSpendDTO).toList();
  }

  @Override
  public SpendDTO createSpend(SpendDTO spend) {
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
    return DTOMapper.mapSpendToSpendDTO(newSpend);
  }
}
