package org.jmxdatamart.common;

import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * @author Xiao Han - Original code
 * @author Binh Tran - check null before shutdown
 */
public class HypersqlHandler extends DBHandler {

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
  private final String driver = "org.hsqldb.jdbcDriver";
  private final String protocol = "jdbc:hsqldb:";
  private final String tableSchem = "public";

  @Override
  public String getTableSchem() {
    return tableSchem;
  }

  public void shutdownDatabase(Connection conn) {
    try {
      if (conn == null || conn.isClosed()) {
        return;
      }
      conn.createStatement().execute("SHUTDOWN");
    } catch (SQLException se) {
      logger.error("Can't shutdown the database:" + se.getMessage());
    }
  }

  @Override
  public Connection connectDatabase(String databaseName, Properties p) {
    try {
      return DriverManager.getConnection(protocol + databaseName, p);
    } catch (SQLException se) {
      logger.error("Can't create the HeyperSql database:" + se.getMessage());
      return null;
    }
  }

  @Override
  public boolean databaseExists(String databaseName, Properties p) {
    //Maybe it is a dummy way to check if a db exits, need to improve
    try {
      DriverManager.getConnection(protocol + databaseName + ";ifexists=true;create=false", p);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public String getDriver() {
    return driver;
  }
}
