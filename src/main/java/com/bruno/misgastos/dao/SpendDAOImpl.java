package com.bruno.misgastos.dao;

import com.bruno.misgastos.dto.Spend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpendDAOImpl implements SpendDAO {

  private static final Logger logger = LoggerFactory.getLogger(SpendDAOImpl.class);

  private static final String LIST_QUERY =
      """
            SELECT
                id,
                date,
                category_id,
                subcategory_id,
                group_id,
                account_id,
                description,
                value
            FROM `spend`
    """;

  private final DataSource datasource;

  @Autowired
  public SpendDAOImpl(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public List<Spend> list() {
    List<Spend> result = new ArrayList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = datasource.getConnection();
      ps = con.prepareStatement(LIST_QUERY);
      rs = ps.executeQuery();
      while (rs.next()) {
        Spend spend =
            new Spend(
                rs.getInt("id"),
                rs.getDate("date"),
                rs.getInt("category_id"),
                rs.getInt("subcategory_id"),
                rs.getInt("group_id"),
                rs.getInt("group_id"),
                rs.getString("description"),
                rs.getDouble("value"));
        result.add(spend);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error obtaining categories", e);
    } finally {
      try {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (con != null) con.close();
      } catch (SQLException e) {
        logger.warn("Error obtaining categories", e);
      }
    }
    return result;
  }
}
