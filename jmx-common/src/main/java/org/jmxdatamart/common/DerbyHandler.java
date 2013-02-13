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
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class DerbyHandler extends DBHandler{
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String protocol = "jdbc:derby:";
    private String timeType = "timestamp";
    public String getTimeType() {
		return null;
	}
    public void shutdownDatabase(String DBName){
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

    public Connection connectDatabase(String DBName,java.util.Properties p) throws SQLException{
        if (!databaseExists(DBName,p))
            return DriverManager.getConnection(protocol + DBName + ";create=true", p);
        else
            return DriverManager.getConnection(protocol + DBName , p);
    }

    public boolean tableExists(String tablename, Connection conn)  throws SQLException{

        String[] names = { "TABLE"};
        ResultSet tableNames = conn.getMetaData().getTables( null, null, null, names);

        while( tableNames.next())
        {
            String tab = tableNames.getString( "TABLE_NAME");
            if (tab.equalsIgnoreCase(tablename)) return true;
        }
        return false;
    }

    public boolean databaseExists(String DBName,java.util.Properties p){
        //Maybe it is a dummy way to check if a db exits, need to improve
        try {
            DriverManager.getConnection(protocol+DBName+ ";create=true", p);
            return true;
        } catch (SQLException e) {
            return false;
        }
   }
    public Map<String, Map> getDatabaseSchema(Connection conn) throws DBException{
        throw new DBException("To be implemented");
    }

    public boolean columnExists(String columnName, String tableName, Connection conn){
        //temporarily not implemented
        return false;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDriver() {
        return driver;
    }
}
