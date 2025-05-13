package com.bruno.misgastos.respositories;

import com.bruno.misgastos.dto.AutocompleteQueryProjectionDTO;
import com.bruno.misgastos.entities.Spend;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpendSpringDataRepository extends JpaRepository<Spend, Integer> {
  @Query(
      """
    SELECT new com.bruno.misgastos.dto.AutocompleteQueryProjectionDTO(s.description, COUNT(*))
    FROM Spend s WHERE s.description LIKE CONCAT('%', :query, '%')
    GROUP BY s.description
    ORDER BY COUNT(*) DESC
    """)
  List<AutocompleteQueryProjectionDTO> getDescriptions(@Param("query") String query);
}
