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

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class MssqlHandler extends DBHandler {
    private final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String jdbcurl ;
    private final String timeType = "datetime";

    public String getTimeType() {
        return timeType;
    }

    public boolean connectServer( Properties p) throws SQLException{
        try {
            Class.forName(this.driver);
            DriverManager.getConnection(this.jdbcurl, p);
        }
        catch (ClassNotFoundException ce){
            return false;
        }
        return true;
    }

    public Map<String, Map> getDatabaseSchema(Connection conn) throws SQLException{


        Map<String, Map> databaseSchema =  new HashMap<String,Map>();

        String[] names = { "TABLE"};
        ResultSet tables = conn.getMetaData().getTables(null, null, null, names), columns =null;

        while( tables.next())
        {
            String tab = tables.getString( "TABLE_NAME");
            String schem = tables.getString("table_schem");
            if (!schem.equalsIgnoreCase("dbo")) continue;
            //System.out.println(tab);
            columns = conn.getMetaData().getColumns(null, null, tab.toUpperCase(), null);
            Map<String, FieldAttribute> fields = new HashMap<String, FieldAttribute>();
            while (columns.next()){
                String col = columns.getString("COLUMN_NAME");
                String typename = columns.getString("TYPE_NAME");
                int type = columns.getInt("DATA_TYPE");
                int size = columns.getInt("COLUMN_SIZE") ;
                DataType myType ;
                switch (type){
                    case Types.VARCHAR:
                        myType = DataType.STRING;
                        break;
                    case Types.INTEGER:
                        myType = DataType.INT;
                        break;
                    case Types.FLOAT:
                    case Types.DOUBLE:
                        myType = DataType.FLOAT;
                        break;
                    default:
                        myType = DataType.STRING;
                        break;
                }
                FieldAttribute fieldinfo = new FieldAttribute(myType,typename,size);
                fields.put(col,fieldinfo);
            }
            databaseSchema.put(tab,fields);
        }

        if (columns!=null) columns.close();
        if (tables!=null) tables.close();

        return databaseSchema;

    }
    public Connection connectDatabase(String databasename, Properties p) throws SQLException {
        Connection conn =null;
        PreparedStatement ps = null;
        ResultSet rs =  null;
        try {
            Class.forName(this.driver);

            conn = DriverManager.getConnection(this.jdbcurl, p);
            String sql = "Select count(*) from master.sys.databases where name=? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, databasename);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1)==0){
                sql = "CREATE DATABASE " + databasename;

                conn.createStatement().executeUpdate(sql);
            }
            return DriverManager.getConnection(this.jdbcurl+";database="+databasename, p);
        }
        catch (ClassNotFoundException ce){
            return null;
        }
        finally {
            if (rs!=null) rs.close();
            if (ps!=null) ps.close();
        }
    }



    public boolean databaseExists(String databaseName,Properties p) throws SQLException{
        Connection conn =null;
        PreparedStatement ps = null;
        ResultSet rs =  null;
        try {
            Class.forName(this.driver);
            conn = DriverManager.getConnection(this.jdbcurl, p);
            String sql = "Select count(*) from master.sys.databases where name=? ";
            ps =conn.prepareStatement(sql);
            ps.setString(1,databaseName);
            rs = ps.executeQuery();
            return (rs.next() && rs.getInt(1)==1);
        }
        catch (ClassNotFoundException ce){
            return false;
        }
        catch (SQLException e){
            return false;
        }
        finally {
            if (ps != null) {ps.close();}
            if (rs != null) {rs.close();}
            if (conn != null) {conn.close();}
        }
    }

    public void setJdbcurl(String jdbcurl) {
        this.jdbcurl = jdbcurl;
    }
}
