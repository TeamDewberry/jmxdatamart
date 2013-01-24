package org.jmxdatamart.Extractor;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class HypersqlHandler extends DBHandler {
    private final String driver = "org.hsqldb.jdbcDriver";
    private final String protocol = "jdbc:hsqldb:";
    private DatabaseMetaData metadata =null;

    public void shutdownDB(Connection conn) throws SQLException{
        conn.createStatement().execute("SHUTDOWN");
    }

    public Connection connectDB(String DBName,java.util.Properties p) throws SQLException{
            return  DriverManager.getConnection(protocol+DBName, p.getProperty("username"),p.getProperty("password"));
    }

    public boolean tblExists(String tablename, Connection conn)  throws SQLException{
        metadata = conn.getMetaData();
        String[] names = { "TABLE"};
        ResultSet tableNames = metadata.getTables( null, null, null, names);

        while( tableNames.next())
        {
            String tab = tableNames.getString( "TABLE_NAME");
            if (tab.equalsIgnoreCase(tablename)) return true;
        }
        return false;
    }

    public boolean dbExists(String DBName,java.util.Properties p){
        //Maybe it is a dummy way to check if a db exits, need to improve
        try {
            DriverManager.getConnection(protocol+DBName, p.getProperty("username"),p.getProperty("password"));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDriver() {
        return driver;
    }
}
