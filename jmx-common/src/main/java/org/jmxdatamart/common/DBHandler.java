/*
 * Copyright (c) 2013, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jmxdatamart.common;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA. User: Xiao Han To change this template use File |
 * Settings | File Templates.
 */
public abstract class DBHandler {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DBHandler.class);

  public abstract boolean databaseExists(String databaseName, java.util.Properties p);

  public abstract Connection connectDatabase(String databaseName, java.util.Properties p);

  public abstract String getTableSchema();

  protected String jdbcurl;


    /**
   * Used default filed to build a table
   *
   * @param conn
   * @param tableName
   * @param databaseType
   */
  public static void addTable(Connection conn, String tableName, DataType.SupportedDatabase databaseType) {
    FieldAttribute autoID = new FieldAttribute("AutoID", DataType.LONG, false);
    addTable(conn, tableName, autoID, databaseType);
  }

  /**
   * Create a table with a given table name in a given database connection
   *
   * @param conn
   * @param tableName
   * @param column: Must have at least one column when creating a new table In
   * this project, if column is null, we create a AutoID column otherwise,
   * create a testID column
   * @param databaseType use to determine which dataType should be used to
   * create the column
   */
  public static void addTable(Connection conn, String tableName, FieldAttribute column, DataType.SupportedDatabase databaseType) {
    PreparedStatement ps = null;
    try {
      checkConnection(conn);
      StringBuilder sql = new StringBuilder();
      String addPK = " ,primary key(" + column.getFieldName() + "))";
      sql.append("create table ");
      sql.append(tableName);
      sql.append(" (");
      sql.append(column.getFieldName());
      sql.append(" ");
      sql.append(column.getFieldType().getType(databaseType).toString());
      sql.append(column.isPrimaryKey() ? addPK : ")");
      ps = conn.prepareStatement(sql.toString());
      ps.executeUpdate();
    } catch (DBException de) {
      logger.error("Can't connect to database.", de);
    } catch (SQLException se) {
      logger.error("Can't create table: " + se.getMessage(), se);
    } finally {
      releaseDatabaseResource(null, null, ps, null);
    }
  }

  /**
   * add specific column into a given table with a given database connection
   *
   * @param conn
   * @param tableName
   * @param column
   * @param databaseType use to determine which dataType should be used to
   * create the column
   */
  public static void addColumn(Connection conn, String tableName, FieldAttribute column, DataType.SupportedDatabase databaseType) {
    PreparedStatement ps = null;
    try {
      if (!tableExists(tableName, conn)) {
        logger.info("Can't find the table.");
        throw new SQLException("Can't find the table.");
      }
      StringBuilder sql = new StringBuilder();
      sql.append("Alter table ").
              append(tableName).
              append(" add ").
              append(column.getFieldName()).
              append(" ").
              append((String) column.getFieldType().getType(databaseType));

      ps = conn.prepareStatement(sql.toString());
      ps.executeUpdate();
    } catch (SQLException se) {
      logger.error("Can't create column:" + se.getMessage(), se);
    } finally {
      releaseDatabaseResource(null, null, ps, null);
    }

  }

  /**
   * For embedded database, it doesn't need to connect the server. For SQL
   * server, we need to connect to the server before further operations. Will
   * override in MssqlHandler.java
   *
   * @param p is the property that holds the username and password
   * @return true if successfully connect to server, otherwise false
   * @throws SQLException
   */
  public boolean connectServer(Properties p) {
    return true;
  }
  /**
   * Check if the given connection is valid
   *
   * @param conn
   * @throws SQLException if the given connection isn't valid
   */
  public static void checkConnection(Connection conn) throws DBException, SQLException {
    if (conn == null || conn.isClosed() || conn.isReadOnly()) {
      throw new DBException("Can't connect to database.");
    }
  }

  /**
   * Get all fields attributes in a given table
   *
   * @param conn is the given connection of a database
   * @param tableName is the given table name
   * @return a Map which the key is the field name, and the value is a
   * FieldAttribute object for that field
   * @throws SQLException
   */
  private static Map<String, FieldAttribute> getTableFields(Connection conn, String tableName, DataType.SupportedDatabase databaseType) {
    ResultSet columns = null;
    try {
      checkConnection(conn);
      String colName;
      DataType typeName;
      int typeId;
      columns = conn.getMetaData().getColumns(null, null, tableName.toUpperCase(), null);
      Map<String, FieldAttribute> fields = new HashMap<String, FieldAttribute>();
      while (columns.next()) {

        colName = columns.getString("COLUMN_NAME");
        typeId = columns.getInt("DATA_TYPE");
        typeName = DataType.getCorrespondDataTypeByID(typeId);

        FieldAttribute fieldinfo = new FieldAttribute(colName, typeName, false);
        fields.put(colName.toUpperCase(), fieldinfo);
      }
      return fields;
    } catch (DBException de) {
      logger.error("Can't connect to database.", de);
      return null;
    } catch (SQLException se) {
      return null;
    } finally {
      releaseDatabaseResource(columns, null, null, null);
    }
  }

  /**
   * Get the next testID(maxID plus one) from a given database connection
   *
   * @param conn
   * @param tableName
   * @param columnName
   * @return
   */
  public static int getMaxTestID(Connection conn, String tableName, String columnName) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      if (!columnExists(columnName, tableName, conn)) {
        return 1;
      }
      ps = conn.prepareStatement("select max(" + columnName + ") from " + tableName);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) + 1;
      } else {
        return 1;
      }
    } catch (SQLException se) {
      logger.error(se.getMessage(), se);
      return 0;
    } finally {
      releaseDatabaseResource(rs, null, ps, null);
    }
  }

  /**
   * Get the schema of a given database
   *
   * @param conn
   * @param tableSchem
   * @return a map object, which key is the table name, and the value is another
   * map that holds the schema of that table
   * @throws SQLException
   */
  public static Map<String, Map> getDatabaseSchema(Connection conn, String tableSchem, DataType.SupportedDatabase databaseType) {
    ResultSet tables = null;
    try {
      checkConnection(conn);
      Map<String, Map> databaseSchema = new HashMap<String, Map>();

      String[] names = {"TABLE"};
      tables = conn.getMetaData().getTables(null, null, null, names);

      while (tables.next()) {
        String tableName = tables.getString("TABLE_NAME");
        String schem = tables.getString("table_schem");
        if (!schem.equalsIgnoreCase(tableSchem)) {
          continue;
        }
        databaseSchema.put(tableName.toUpperCase(), getTableFields(conn, tableName, databaseType));
      }
      return databaseSchema;
    } catch (DBException de) {
      logger.error("Can't connect to database.", de);
      return null;
    } catch (SQLException se) {
      return null;
    } finally {
      releaseDatabaseResource(tables, null, null, null);
    }
  }

  /**
   * Check if a given table exits in a given database
   *
   * @param tableName is the table looking for
   * @param conn is the given database connection
   * @return true if the table exists, otherwise false
   * @throws SQLException
   */
  public static boolean tableExists(String tableName, Connection conn) {
    ResultSet tableNames = null;
    try {
      checkConnection(conn);

      String[] names = {"TABLE"};
      tableNames = conn.getMetaData().getTables(null, null, null, names);

      while (tableNames.next()) {
        String tab = tableNames.getString("TABLE_NAME");
        if (tab.equalsIgnoreCase(tableName)) {
          tableNames.close();
          return true;
        }
      }
      tableNames.close();
      return false;
    } catch (DBException de) {
      logger.error("Can't connect to database.", de);
      throw new RuntimeException(de);
    } catch (SQLException se) {
      return false;
    } finally {
      releaseDatabaseResource(tableNames, null, null, null);
    }
  }

  /**
   * Check if a given column exists in a given table
   *
   * @param columnName is the column looking for
   * @param tableName is the given table name
   * @param conn is the given database connection
   * @return true if the column exist, otherwise false
   * @throws SQLException
   */
  public static boolean columnExists(String columnName, String tableName, Connection conn) {
    if (!tableExists(tableName, conn)) {
      return false;
    }

    ResultSet columnNames = null;
    try {
      columnNames = conn.getMetaData().getColumns(null, null, tableName.toUpperCase(), columnName.toUpperCase());
      while (columnNames.next()) {
        String col = columnNames.getString("COLUMN_NAME");
        if (col.equalsIgnoreCase(columnName)) {
          columnNames.close();
          return true;
        }
      }
      columnNames.close();
      return false;
    } catch (SQLException se) {
      return false;
    } finally {
      releaseDatabaseResource(columnNames, null, null, null);
    }
  }


  /**
   * Release all resources related to database operation - argument can be null
   * in which case it will be ignored.
   * 
   * @param rs
   * @param st
   * @param ps
   * @param conn
   */
  public static void releaseDatabaseResource(ResultSet rs, Statement st, PreparedStatement ps, Connection conn) {

    // PrepareStatement
    try {
      if (ps != null) {
        ps.close();
      }
    } catch (SQLException sqle) {
      logSQLException(sqle);
    }

    // ResultSet
    try {
      if (rs != null) {
        rs.close();
      }
    } catch (SQLException sqle) {
      logSQLException(sqle);
    }

    //Statement
    try {
      if (st != null) {
        st.close();
      }
    } catch (SQLException sqle) {
      logSQLException(sqle);
    }

    //Connection
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException sqle) {
      logSQLException(sqle);
    }

  }

  /**
   * Log sql related errors
   *
   * @param e
   */
  protected static void logSQLException(SQLException e) {
    while (e != null) {
      logger.error("SQLException-- State:" + e.getSQLState()
              + "\tMessage:" + e.getMessage(), e);
      e = e.getNextException();
    }
  }

  /**
   * Load jdbc driver
   *
   * @param driver is the given driver string
   */
  public void loadDriver(String driver) {
    try {
      Class.forName(driver).newInstance();
    } catch (ClassNotFoundException cnfe) {
      logger.error("Unable to load the JDBC driver " + driver, cnfe);
      throw new RuntimeException(cnfe);
    } catch (InstantiationException ie) {
      logger.error("Unable to instantiate the JDBC driver " + driver, ie);
      throw new RuntimeException(ie);
    } catch (IllegalAccessException iae) {
      logger.error("Not allowed to access the JDBC driver " + driver, iae);
      throw new RuntimeException(iae);
    }
  }
}
