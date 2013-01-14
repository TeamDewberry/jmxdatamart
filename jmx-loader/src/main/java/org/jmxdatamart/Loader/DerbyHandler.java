package org.jmxdatamart.Loader;


import java.sql.*;
/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class DerbyHandler extends DBHandler{
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String protocol = "jdbc:derby:";
    private DatabaseMetaData metadata =null;

    public void shutdownDB(String DBName){
        try
        {
            DriverManager.getConnection("jdbc:derby:" + DBName + ";shutdown=true");

        }
        catch (SQLException se)
        {
            if (( (se.getErrorCode() == 45000)
                    && ("08006".equals(se.getSQLState()) ))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                super.printSQLException(se);
            }
        }
    }

    public Connection connectDB(String DBName,java.util.Properties p) throws SQLException{
        if (!dbExists(DBName,p))
            return DriverManager.getConnection(protocol + DBName + ";create=true", p);
        else
            return DriverManager.getConnection(protocol + DBName , p);
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
        //Maybe it is a dummy way to check if a db exits, need to improve
        try {
            DriverManager.getConnection(protocol+DBName+ ";create=true", p);
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
