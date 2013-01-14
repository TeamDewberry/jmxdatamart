package org.jmxdatamart.Loader;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class MssqlHandler extends DBHandler {
    private final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String protocol = "jdbc:sqlserver://localhost:1433";
    private DatabaseMetaData metadata =null;

    public Connection connectDB(String DBName,java.util.Properties p) throws SQLException {
        //to be finished
        return null;
/*        if (!dbExists(DBName,p))
            return DriverManager.getConnection(protocol + DBName + ";create=true", p);
        else
            return DriverManager.getConnection(protocol + DBName , p);

        readConfig();

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
        String sql = "Select count(*) from master.sys.databases where name='" +DB_NAME +"' ";
        rs = stmt.executeQuery(sql);
        rs.next();
        if (rs.getInt(1)==0){
            sql = "CREATE DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);
        }
        stmt.close();
        conn.close();
        conn = DriverManager.getConnection(DB_URL+";database="+DB_NAME, USER, PASS);
        stmt = conn.createStatement(); */
    }

    public boolean tblExists(String TblName, Connection conn)  throws SQLException{
        metadata = conn.getMetaData();
        String[] names = { "TABLE"};
        ResultSet tableNames = metadata.getTables( null, null, null, names);

        while( tableNames.next())
        {
            String tab = tableNames.getString( "TABLE_NAME");
            if (tab.equalsIgnoreCase(TblName)) return true;
        }
        return false;
    }

    public boolean dbExists(String DBName,java.util.Properties p){
        //to be finished
        return true;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDriver() {
        return driver;
    }
}
