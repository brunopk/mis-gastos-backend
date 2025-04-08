package com.bruno.misgastos.dao;

import com.bruno.misgastos.dto.Group;
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
public class GroupDAOImpl implements GroupDAO {

  private static final Logger logger = LoggerFactory.getLogger(GroupDAOImpl.class);

  private static final String LIST_QUERY = "SELECT * FROM `group`";

  private final DataSource datasource;

  @Autowired
  public GroupDAOImpl(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public List<Group> list() {
    List<Group> result = new ArrayList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = datasource.getConnection();
      ps = con.prepareStatement(LIST_QUERY);
      rs = ps.executeQuery();
      while (rs.next()) {
        Group group = new Group(rs.getInt("id"), rs.getString("name"), rs.getInt("subcategory_id"));
        result.add(group);
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
