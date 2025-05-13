package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.IncomeDTO;
import com.bruno.misgastos.entities.Income;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.exceptions.ResourceNotFoundException;
import com.bruno.misgastos.respositories.IncomeSpringDataRepository;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.utils.DTOMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomesServiceImpl implements IncomesService {

  private final IncomeSpringDataRepository incomeRepository;

  private final SpendSpringDataRepository spendRepository;

  @Autowired
  public IncomesServiceImpl(
      IncomeSpringDataRepository incomeRepository, SpendSpringDataRepository spendRepository) {
    this.incomeRepository = incomeRepository;
    this.spendRepository = spendRepository;
  }

  @Override
  public List<IncomeDTO> getIncomes() {
    return incomeRepository.findAll().stream().map(this::mapIncomeToIncomeDTO).toList();
  }

  @Override
  @Transactional
  public IncomeDTO createIncome(IncomeDTO income) {
    Optional<Spend> spend =
        income.spend() != null ? spendRepository.findById(income.spend().id()) : Optional.empty();
    if (Objects.nonNull(income.spend()) && spend.isEmpty())
      throw new ResourceNotFoundException(Spend.class);

    Income newIncome =
        incomeRepository.save(
            new Income(
                income.date(),
                income.incomeTypeId(),
                income.accountId(),
                Objects.nonNull(income.spend()) ? spend.get() : null,
                income.description(),
                income.value()));

    return mapIncomeToIncomeDTO(newIncome);
  }

  private IncomeDTO mapIncomeToIncomeDTO(Income income) {
    return new IncomeDTO(
        income.getId(),
        income.getDate(),
        income.getIncomeTypeId(),
        income.getAccountId(),
        Objects.nonNull(income.getSpend()) ? DTOMapper.mapSpendToSpendDTO(income.getSpend()) : null,
        income.getDescription(),
        income.getValue());
  }
}
