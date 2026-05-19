package com.bruno.misgastos.services;

import com.bruno.misgastos.dto.IncomeDto;
import com.bruno.misgastos.entities.Income;
import com.bruno.misgastos.entities.Spend;
import com.bruno.misgastos.exceptions.ResourceNotFoundException;
import com.bruno.misgastos.respositories.IncomeSpringDataRepository;
import com.bruno.misgastos.respositories.SpendSpringDataRepository;
import com.bruno.misgastos.utils.DtoMapper;
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
  public List<IncomeDto> getIncomes() {
    return incomeRepository.findAll().stream().map(this::mapIncomeToIncomeDTO).toList();
  }

  @Override
  @Transactional
  public IncomeDto createIncome(IncomeDto income) {
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

  private IncomeDto mapIncomeToIncomeDTO(Income income) {
    return new IncomeDto(
        income.getId(),
        income.getDate(),
        income.getIncomeTypeId(),
        income.getAccountId(),
        Objects.nonNull(income.getSpend()) ? DtoMapper.mapSpendToSpendDTO(income.getSpend()) : null,
        income.getDescription(),
        income.getValue());
  }
}
