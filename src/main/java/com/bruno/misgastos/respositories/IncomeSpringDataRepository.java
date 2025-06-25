package com.bruno.misgastos.respositories;

import com.bruno.misgastos.dto.AutocompleteQueryProjectionDto;
import com.bruno.misgastos.entities.Income;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncomeSpringDataRepository extends JpaRepository<Income, Integer> {
  @Query(
      """
    SELECT new com.bruno.misgastos.dto.AutocompleteQueryProjectionDTO(i.description, COUNT(*))
    FROM Income i WHERE i.description LIKE CONCAT('%', :query, '%')
    GROUP BY i.description
    ORDER BY COUNT(*) DESC
    """)
  List<AutocompleteQueryProjectionDto> getDescriptions(@Param("query") String query);
}
