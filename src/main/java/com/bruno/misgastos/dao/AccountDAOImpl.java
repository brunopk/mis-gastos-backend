package com.bruno.misgastos.dao;

import com.bruno.misgastos.dto.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAOImpl implements AccountDAO {

  private static final Logger logger = LoggerFactory.getLogger(AccountDAOImpl.class);

  private static final String LIST_QUERY = "SELECT * FROM `account`";

  private final DataSource datasource;

  @Autowired
  public AccountDAOImpl(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public List<Account> list() {
    List<Account> result = new ArrayList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = datasource.getConnection();
      ps = con.prepareStatement(LIST_QUERY);
      rs = ps.executeQuery();
      while (rs.next()) {
        Account account = new Account(rs.getInt("id"), rs.getString("name"));
        result.add(account);
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
