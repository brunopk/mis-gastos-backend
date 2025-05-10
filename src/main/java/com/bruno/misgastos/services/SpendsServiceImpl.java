package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.SpendDTO;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
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
    return spendRepository.findAll().stream()
        .map(
            spend ->
                new SpendDTO(
                    spend.getId(),
                    spend.getDate(),
                    spend.getCategoryId(),
                    spend.getSubcategoryId(),
                    spend.getGroupId(),
                    spend.getAccountId(),
                    spend.getDescription(),
                    spend.getValue()))
        .toList();
  }

  @Override
  public SpendDTO createSpend(SpendDTO spend) {
    Spend newSpend = spendRepository.save(new Spend(
      spend.date(),
      spend.categoryId(),
      spend.subcategoryId(),
      spend.groupId(),
      spend.accountId(),
      spend.description(),
      spend.value()));
    return new SpendDTO(newSpend.getId(), newSpend.getDate(), newSpend.getCategoryId(), newSpend.getSubcategoryId(), newSpend.getGroupId(), newSpend.getAccountId(), newSpend.getDescription(), newSpend.getValue());
  }
}
