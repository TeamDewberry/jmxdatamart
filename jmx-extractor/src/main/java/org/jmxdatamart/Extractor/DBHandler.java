package org.jmxdatamart.Extractor;

import java.sql.*;
/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public abstract class DBHandler {


    public abstract boolean dbExists(String DBName,java.util.Properties p);
    public abstract boolean tblExists(String TblName, Connection conn) throws SQLException;

    public abstract Connection connectDB(String DBName,java.util.Properties p) throws SQLException;
    //public abstract void dropDB(String DBName);
    public void disconnectDB(ResultSet rs, Statement st, PreparedStatement ps, Connection conn){

        // PrepareStatement
        try {
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        // ResultSet
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        //Statement
        try {
            if (st != null) {
                st.close();
                st = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        //Connection
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

    }


    protected void printSQLException(SQLException e)
    {
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            e = e.getNextException();
        }
    }


    protected void loadDriver(String driver) {
        try {
            Class.forName(driver).newInstance();
            System.out.println("Loaded the appropriate driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver " + driver);
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
            System.exit(1);
        } catch (InstantiationException ie) {
            System.err.println(
                    "\nUnable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
            System.exit(1);
        } catch (IllegalAccessException iae) {
            System.err.println(
                    "\nNot allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
