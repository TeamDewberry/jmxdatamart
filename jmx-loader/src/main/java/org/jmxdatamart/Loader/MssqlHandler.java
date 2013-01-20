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
