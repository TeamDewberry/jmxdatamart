package org.jmxdatamart.common;

import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class HypersqlHandler extends DBHandler {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String driver = "org.hsqldb.jdbcDriver";
    private final String protocol = "jdbc:hsqldb:";
    private final String tableSchem = "public";

    public String getTableSchem() {
        return tableSchem;
    }

    public void shutdownDatabase(Connection conn){
        try{
            conn.createStatement().execute("SHUTDOWN");
        }
        catch (SQLException se){
            logger.error("Can't shutdown the database:" + se.getMessage());
        }
    }

    public Connection connectDatabase(String databaseName,Properties p){
        try{
            return  DriverManager.getConnection(protocol+databaseName, p);
        }
        catch (SQLException se){
            logger.error("Can't create the HeyperSql database:" + se.getMessage());
            return null;
        }
    }


    public boolean databaseExists(String databaseName,Properties p){
        //Maybe it is a dummy way to check if a db exits, need to improve
        try {
            DriverManager.getConnection(protocol+databaseName+";ifexists=true;create=false", p);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getDriver() {
        return driver;
    }
}
