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
import org.slf4j.LoggerFactory;
/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public abstract class DBHandler {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DBHandler.class);
    public abstract boolean databaseExists(String DBName,java.util.Properties p);
    public abstract boolean tableExists(String TblName, Connection conn) throws SQLException;

    public abstract Connection connectDatabase(String DBName,java.util.Properties p) throws SQLException;
    //public abstract void dropDB(String DBName);
    public void disconnectDatabase(ResultSet rs, Statement st, PreparedStatement ps, Connection conn){

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
            /*
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            */
            logger.error("SQLException-- State:" + e.getSQLState()+
                        "\tMessage:" + e.getMessage() ,e);
            e = e.getNextException();
        }
    }


    public void loadDriver(String driver) {
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
