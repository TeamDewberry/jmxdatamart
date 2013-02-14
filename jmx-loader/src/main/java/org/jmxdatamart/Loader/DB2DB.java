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

import org.jmxdatamart.common.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;


public class DB2DB {
    private final String mainTableName = "mainTable";
    private final String idName = "testID";
    private final String idNameType = "varchar(40)";
    private final String importTime = "importTime";
    private final String dbfile = "dbfile"; //use to check if the embedded database has been imported, avoid duplicated import.
    private final String dbfileType = "varchar(100)";

    private DBHandler sourceDatabase, targerDatabase;
    private Connection sourceConn,targetConn;
    private Properties required, optional;
    private String importfile;
    private Map<String,Map> sourceSchema;

    public LoaderSetting readProperties(String filePath) {
        LoaderSetting s = new LoaderSetting();
        LoaderSetting.DBInfo source = s.new DBInfo() , target = s.new DBInfo();
        Properties re =new Properties(), op = new Properties(), sp =new Properties(), tp = new Properties();
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);

            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String Property = props.getProperty(key);
                String section = key.split("\\.")[0];
                String keyname = key.split("\\.")[1];
                if (section.equalsIgnoreCase("required"))
                    re.put(keyname, Property);
                else if(section.equalsIgnoreCase("optional"))
                    op.put(keyname, Property);
                else{
                    if (key.equalsIgnoreCase("source.type"))
                        source.setDatabaseType(Property);
                    else if(key.equalsIgnoreCase("source.JDBCurl"))
                        source.setJdbcUrl(Property);
                    else if(key.equalsIgnoreCase("source.databasename"))
                        source.setDatabaseName(Property);
                    else if(key.equalsIgnoreCase("source.user"))
                        sp.put("user", Property);
                    else if(key.equalsIgnoreCase("source.password"))
                        sp.put("password", Property);
                    else if (key.equalsIgnoreCase("target.type"))
                        target.setDatabaseType(Property);
                    else if(key.equalsIgnoreCase("target.JDBCurl"))
                        target.setJdbcUrl(Property);
                    else if(key.equalsIgnoreCase("target.databasename"))
                        target.setDatabaseName(Property);
                    else if(key.equalsIgnoreCase("target.user"))
                        tp.put("user",Property);
                    else if(key.equalsIgnoreCase("target.password"))
                        tp.put("password", Property);
                }
            }
            source.setUserInfo(sp);
            target.setUserInfo(tp);
            s.setSource(source);
            s.setTarget(target);
            s.setRequired(re);
            s.setOptional(op);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public void disConnect() throws SQLException{
        DBHandler.disconnectDatabase(null,null,null,sourceConn);
        DBHandler.disconnectDatabase(null,null,null,targetConn);
    }
    public void loadSetting(LoaderSetting s) throws SQLException,DBException {


        if (!s.getSource().getJdbcUrl().toLowerCase().contains(s.getSource().getDatabaseType().toLowerCase()))
            throw new DBException("The source section in setting file must be wrong!");
        if (!s.getTarget().getJdbcUrl().toLowerCase().contains(s.getTarget().getDatabaseType().toLowerCase()))
            throw new DBException("The target section in setting file must be wrong!");

        if (s.getSource().getDatabaseType().equalsIgnoreCase("hsqldb"))
            sourceDatabase = new HypersqlHandler();
        else if(s.getSource().getDatabaseType().equalsIgnoreCase("sqlserver")){
            sourceDatabase = new MssqlHandler();
            ((MssqlHandler)sourceDatabase).setJdbcurl(s.getSource().getJdbcUrl());        }
        else
            throw new DBException("Only support HyperSQL and MSSQL so for!");

        if (s.getTarget().getDatabaseType().equalsIgnoreCase("hsqldb")){
        	targerDatabase = new HypersqlHandler();
        }
        else if(s.getTarget().getDatabaseType().equalsIgnoreCase("sqlserver")){
        	targerDatabase = new MssqlHandler();
        	((MssqlHandler)targerDatabase).setJdbcurl(s.getTarget().getJdbcUrl());
        }
        else
            throw new DBException("Only support HyperSQL and MSSQL so for!");

        //source database must exist
        if (sourceDatabase.connectServer(s.getSource().getUserInfo()))
            if (sourceDatabase.databaseExists(s.getSource().getDatabaseName(),s.getSource().getUserInfo()) )
                sourceConn = sourceDatabase.connectDatabase(s.getSource().getDatabaseName(),s.getSource().getUserInfo());
            else
                throw new DBException("Can't connect to source database!");
        else
            throw new DBException("Can't connect to the source server, please check database driver, network, username, password and etc...");

        //if target database doesn't exist, then create one. But prerequisite is be able to connect the server.
        if (targerDatabase.connectServer(s.getTarget().getUserInfo()))
            targetConn = targerDatabase.connectDatabase(s.getTarget().getDatabaseName(),s.getTarget().getUserInfo());
        else
            throw new DBException("Can't connect to the target server, please check database driver, network, username, password and etc...");

        sourceSchema = sourceDatabase.getDatabaseSchema(sourceConn);

        required = s.getRequired();
        optional = s.getOptional();
        importfile = s.getSource().getDatabaseName();
        
        System.out.println("Loading data from [" +s.getSource().getDatabaseType()+"]"+ importfile + " to [" + s.getTarget().getDatabaseType()+"]"+ s.getTarget().getDatabaseName() + "..." );
    }


    public void copySchema() throws SQLException,DBException {
    	if (sourceSchema==null)
    		throw new DBException("Can't obtain the schema from the souce database!");
        StringBuilder sql ;
        
        Boolean bl = targetConn.getAutoCommit();
        targetConn.setAutoCommit(false);
        for (Map.Entry<String, Map> tables : sourceSchema.entrySet()) {
            String tab = tables.getKey();
            if (tab.equalsIgnoreCase(this.mainTableName)) continue; //in case the source database has the "maintable"
            if (!DBHandler.tableExists(tab,targetConn)){
                sql = new StringBuilder();
                sql.append("create table ").append(tab).append("(").append(idName).append(" ").append(idNameType).append(")");
                //System.out.println(sql.toString());
                targetConn.createStatement().execute(sql.toString());
            }

            Map<String,FieldAttribute>  fieldinfo = (Map<String, FieldAttribute>)tables.getValue();

            for (Map.Entry<String, FieldAttribute> field : fieldinfo.entrySet()) {
                String col = field.getKey();
                if (col.equalsIgnoreCase(this.idName)) continue; //in case the tables in source database has the field named "testid"
                FieldAttribute attributes =field.getValue();
                if (!DBHandler.columnExists(col,tab,targetConn)){
                    sql = new StringBuilder();
                    sql.append("Alter table ").append(tab).append(" add ").append(col).append(" ").append(attributes.getTypename());
                    if (attributes.getFieldtype().equals(DataType.STRING))
                        sql.append("(").append(attributes.getFieldsize()).append(")") ;
                    //System.out.println(sql.toString());
                    targetConn.createStatement().execute(sql.toString());
                }

            }
            
        }

        targetConn.commit();

        targetConn.setAutoCommit(bl);


        createMainTable();
    }

    public void createMainTable() throws SQLException{
        StringBuilder sql;
        boolean bl = targetConn.getAutoCommit();

        targetConn.setAutoCommit(false);
        if (!DBHandler.tableExists(mainTableName,targetConn)){
            sql = new StringBuilder();
            sql.append("create table ").append(mainTableName).append("(").append(idName).append(" ").append(idNameType).append(",");
            sql.append(dbfile).append(" ").append(dbfileType).append(",");
            sql.append(importTime).append(" ").append(targerDatabase.getTimeType()).append(" not null, primary key(").append(idName).append("))");
            //System.out.println(sql.toString());
            targetConn.createStatement().executeUpdate(sql.toString());
        }

        Properties merged = new Properties();
        merged.putAll(required);
        merged.putAll(optional);
        Enumeration keys = merged.keys();
        while (keys.hasMoreElements()) {
            String col = (String) keys.nextElement();
            if (!DBHandler.columnExists(col,mainTableName,targetConn)){
                sql = new StringBuilder();
                sql.append("Alter table ").append(mainTableName).append(" add ").append(col).append(" varchar(100)") ;
                //System.out.println(sql.toString());
                targetConn.createStatement().execute(sql.toString());
            }
        }

        targetConn.commit();
        targetConn.setAutoCommit(bl);
    }

    public boolean alreadyImport() throws SQLException{
        if (!DBHandler.tableExists(mainTableName,targetConn)){
            return false;
        }
        else{
            PreparedStatement ps = targetConn.prepareStatement("select count(" + dbfile + ") from " + mainTableName + " where " + dbfile + " = ?");
            ps.setString(1,this.importfile);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1)==0) {
                rs.close();
                ps.close();
                return false;
            }
        }
        return true;
    }

    public void importData() throws SQLException{
        String testid= UUID.randomUUID().toString();
        StringBuilder fieldList, questionMarkList ;
        PreparedStatement ps ;
        ResultSet rs = null;
        String query,insert;
        int fieldCount = 0, tableCount =0, recordCount=0;
        String fields[];
        Boolean bl = targetConn.getAutoCommit();
        targetConn.setAutoCommit(false);
        for (Map.Entry<String, Map> tables : sourceSchema.entrySet()) {
            String tab = tables.getKey();
            //System.out.println(tab);
            if (tab.equalsIgnoreCase(this.mainTableName)) continue; //in case the source database has the "maintable"
            tableCount ++;
            Map<String,FieldAttribute>  fieldinfo = (Map<String, FieldAttribute>)tables.getValue();
            fieldList = new StringBuilder();
            questionMarkList = new StringBuilder();
            for (Map.Entry<String, FieldAttribute> field : fieldinfo.entrySet()) {
                String col = field.getKey();
                if (col.equalsIgnoreCase(this.idName)) continue; //in case the tables in source database has the fields named "testid"
                fieldList.append(col).append(",");
                questionMarkList.append("?,");
            }
            insert = "insert into " + tab + "(" + fieldList.toString()  + idName + ") values(" + questionMarkList + "?)";
            query =  "select " + fieldList.toString() + "1 from " + tab;
            //System.out.println(insert);
            //System.out.println(query);
            rs = sourceConn.createStatement().executeQuery(query);
            ps = targetConn.prepareStatement(insert);
            fields = fieldList.toString().split(",");
            while (rs.next()){
                recordCount ++;
                for (fieldCount =0; fieldCount<fields.length; fieldCount++){
                    String col = fields[fieldCount];
                    FieldAttribute attributes =fieldinfo.get(col);
                    switch (attributes.getFieldtype()){
                        case INT:
                            ps.setInt(fieldCount+1,rs.getInt(col));
                            break;
                        case STRING:
                            ps.setString(fieldCount+1,rs.getString(col));
                            break;
                        case FLOAT:
                            ps.setFloat(fieldCount+1,rs.getFloat(col));
                            break;
                    }
                }
                ps.setString(++fieldCount, testid);
                ps.executeUpdate();
            }
        }
        if (rs!=null) rs.close();

        //update the maintable
        fieldList=new StringBuilder();
        fieldList.append(idName).append(",").append(dbfile).append(",").append(importTime);
        questionMarkList = new StringBuilder("?,?,?");
        Properties merged = new Properties();
        merged.putAll(required);
        merged.putAll(optional);
        Enumeration keys = merged.keys();
        while (keys.hasMoreElements()) {
            String col = (String) keys.nextElement();
            fieldList.append(",").append(col);
            questionMarkList.append(",?");
        }
        insert = "insert into " + mainTableName + "(" + fieldList.toString() + ") values(" + questionMarkList +")";
        //System.out.println(insert);
        ps = targetConn.prepareStatement(insert);
        ps.setString(1,testid);
        ps.setString(2,this.importfile);
        ps.setTimestamp(3,new Timestamp((new java.util.Date()).getTime()));

        fields =fieldList.toString().split(",");
        for (fieldCount=3;fieldCount< fields.length  ; fieldCount++){
            ps.setString(fieldCount+1,merged.getProperty(fields[fieldCount]));
        }
        ps.executeUpdate();

        targetConn.commit();
        targetConn.setAutoCommit(bl);

        if (ps!=null) ps.close();

        System.out.println("Data was loaded successfully!\nTestid:" + testid );
        System.out.println( ++tableCount + " tables and " + ++recordCount + " records were loaded.");


    }
}
