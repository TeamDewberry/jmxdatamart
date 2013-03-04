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
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;


public class DB2DB {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private DataMartDB dataMart;
    private SourceDB sources;

    /**
     * Initial the Data mart setting and the source database setting from the seting file
     * @param s
     * @param folder
     */
    public DB2DB(Setting s, File folder){
        dataMart = new DataMartDB(s.getTarget(),s.getRequired(),s.getOptional());
        sources = new SourceDB(s.getSource(), folder);
    }

    /**
     * Import data from the datafiles to data mart
     * @throws DBException
     * @throws SQLException
     */
    public void importData() throws DBException,SQLException{

        Connection dataMartConnection = connectToDataMartDatabase();
        Connection sourceConnection = null;
        int testId;
        Map<String,Map> sourceDatabaseTables;
        String sourceTableSchem = sources.getSourceDatabase().getTableSchema();
        String sourceDatabaseType = sources.getDbInfo().getDatabaseType();
        String mainTableName = dataMart.getMainTableName();
        String testIDFieldName = dataMart.getTestID().getFieldName();
        boolean bl = dataMartConnection.getAutoCommit();
        dataMartConnection.setAutoCommit(false);

        for(String sourceDatabaseFile : sources.getDatabaseFiles()){
            testId = DBHandler.getMaxTestID(dataMartConnection, mainTableName, testIDFieldName);

            if ((sourceConnection = connectToSourceDatabase(sourceDatabaseFile))==null){
                logger.error("\n" +sourceDatabaseFile + " fail to import to DataMart: it might be an invalid database file.\n");
                continue;
            }
            else{
                logger.info("\nStart loading database file: " + sourceDatabaseFile + ".");
            }

            sourceDatabaseTables = DBHandler.getDatabaseSchema(sourceConnection,sourceTableSchem, sourceDatabaseType);
            copyOthersScheme(dataMartConnection,sourceDatabaseTables);
            loadAllTablesDataExceptMain(sourceConnection, dataMartConnection, testId, sourceDatabaseTables);
            addMainTableScheme(dataMartConnection);
            addMainTableData(dataMartConnection, testId, sourceDatabaseFile);

            dataMartConnection.commit();
            logger.info( sourceDatabaseFile + " is imported to DataMart .\n");
            ((HypersqlHandler)sources.getSourceDatabase()).shutdownDatabase(sourceConnection); //need to improve

        }

        dataMartConnection.setAutoCommit(bl);
        dataMartConnection.close();
        sourceConnection.close();
    }

    /**
     * Try to connect to the datamart
     * @return null if it can't connect to datamart, otherwise return the database connection
     * @throws DBException
     */
    private Connection connectToDataMartDatabase() throws DBException{
        Properties userInfo = dataMart.getDbInfo().getUserInfo();
        String databaseName = dataMart.getDbInfo().getDatabaseName();
        DBHandler dataMartDatabase = dataMart.getTargetDatabase();
        Connection conn = null;

        if (!dataMartDatabase.connectServer(userInfo)){
            throw new DBException("Can't connect to DataMart server.");
        }
        else{
            if ((conn = dataMartDatabase.connectDatabase(databaseName,userInfo)) == null)
                throw new DBException("Can't create the DataMart database");
            return conn;
        }

    }

    /**
     * Try to connect to the source database
     * @param sourceDatabaseFileName
     * @return null if the source database doesn't exist, otherwise return the database connection
     */
    private Connection connectToSourceDatabase(String sourceDatabaseFileName){
        DBHandler sourceDatabase = sources.getSourceDatabase();
        Properties userInfo = sources.getDbInfo().getUserInfo();

        if (sourceDatabase.databaseExists(sourceDatabaseFileName, userInfo))
            return sourceDatabase.connectDatabase(sourceDatabaseFileName, userInfo);
        else
            return null;
    }

    /**
     * Add main table schema to data mart
     * @param dataMartConnection
     */
    private void addMainTableScheme(Connection dataMartConnection){
        String mainTableName = dataMart.getMainTableName();
        String databaseType = dataMart.getDbInfo().getDatabaseType();
        if (!DBHandler.tableExists(mainTableName,dataMartConnection)){
            DBHandler.addTable (dataMartConnection, mainTableName, dataMart.getTestID(), databaseType );
            DBHandler.addColumn(dataMartConnection, mainTableName, dataMart.getImportTime(), databaseType);
            DBHandler.addColumn(dataMartConnection, mainTableName, dataMart.getImportedFile(), databaseType);
        }

        Properties merged = new Properties();
        merged.putAll(dataMart.getRequired());
        merged.putAll(dataMart.getOptional());
        Enumeration keys = merged.keys();
        FieldAttribute field;
        while (keys.hasMoreElements()) {
            String col = (String) keys.nextElement();
            if (!DBHandler.columnExists(col,mainTableName,dataMartConnection)){
                field = new FieldAttribute(col,DataType.STRING,false);
                DBHandler.addColumn(dataMartConnection, mainTableName, field, databaseType);
            }
        }
    }

    /**
     * Add main table infomation
     * @param dataMartConnection
     * @param testId
     * @param sourceDatabaseFile
     */
    private void addMainTableData(Connection dataMartConnection, int testId, String sourceDatabaseFile){
        StringBuilder fieldList=new StringBuilder();
        fieldList.append(dataMart.getTestID().getFieldName())
                 .append(",")
                 .append(dataMart.getImportedFile().getFieldName())
                 .append(",")
                 .append(dataMart.getImportTime().getFieldName());
        StringBuilder questionMarkList = new StringBuilder("?,?,?");

        Properties merged = new Properties();
        merged.putAll(dataMart.getRequired());
        merged.putAll(dataMart.getOptional());
        Enumeration keys = merged.keys();
        String column;
        while (keys.hasMoreElements()) {
            column = (String) keys.nextElement();
            fieldList.append(",").append(column);
            questionMarkList.append(",?");
        }
        String sql = "insert into " + dataMart.getMainTableName() +
                     "(" + fieldList.toString() + ") values(" + questionMarkList +")";
        PreparedStatement ps = null;
        try{
            ps = dataMartConnection.prepareStatement(sql);
            ps.setInt(1, testId);
            ps.setString(2,sourceDatabaseFile);
            ps.setTimestamp(3,new Timestamp((new java.util.Date()).getTime()));

            String fields[] =fieldList.toString().split(",");
            for (int i=3;i < fields.length  ; i++){
                ps.setString(i+1,merged.getProperty(fields[i]));
            }
            ps.executeUpdate();
        }
        catch (SQLException se){
            logger.error(se.getMessage());
        }
        finally {
            DBHandler.releaseDatabaseResource(null,null,ps,null);
        }
    }


    /**
     * Copy the schema from source database to data mart
     * @param dataMartConnection
     * @param sourceDatabaseTables
     */
    private void copyOthersScheme(Connection dataMartConnection, Map<String,Map> sourceDatabaseTables){
        String mainTableName = dataMart.getMainTableName();
        FieldAttribute testIDField = dataMart.getTestID();
        testIDField.setPK(false);
        String testIDFieldName = dataMart.getTestID().getFieldName();
        String sourceDatabaseType = sources.getDbInfo().getDatabaseType();
        String dataMartDatabaseType = dataMart.getDbInfo().getDatabaseType();
        String tableName,columnName;
        Map<String,FieldAttribute>  fields;
        FieldAttribute attributes;
        for (Map.Entry<String, Map> table : sourceDatabaseTables.entrySet() ) {
            tableName = table.getKey();
            if (tableName.equalsIgnoreCase(mainTableName)) continue; //in case the source database has the "maintable"

            if (!DBHandler.tableExists(tableName,dataMartConnection)){
                DBHandler.addTable(dataMartConnection,tableName, testIDField, sourceDatabaseType);
            }

            fields = (Map<String, FieldAttribute>)table.getValue();
            for (Map.Entry<String, FieldAttribute> field : fields.entrySet()) {
                columnName = field.getKey();
                if (columnName.equalsIgnoreCase(testIDFieldName)) continue; //in case the tables in source database has the field named "testid"

                attributes =field.getValue();
                if (!DBHandler.columnExists(columnName,tableName,dataMartConnection)){
                    DBHandler.addColumn(dataMartConnection,tableName,attributes,dataMartDatabaseType);
                }
            }
        }
    }

    /**
     * Load all the data from source database to data mart
     * @param sourceConnection
     * @param dataMartConnection
     * @param testID
     * @param sourceDatabaseTables
     * @throws SQLException
     */
    private void loadAllTablesDataExceptMain(Connection sourceConnection, Connection dataMartConnection, int testID, Map<String,Map> sourceDatabaseTables) throws SQLException{

        int  tableCount =0, recordCount = 0;
        Map<String,FieldAttribute>  fieldInfo;
        String tableName;
        for (Map.Entry<String, Map> table : sourceDatabaseTables.entrySet()) {
            tableName = table.getKey();
            if (tableName.equalsIgnoreCase(dataMart.getMainTableName())) continue; //in case the source database has the "maintable"
            fieldInfo= (Map<String, FieldAttribute>)table.getValue();
            recordCount += loadOneTableData(fieldInfo, tableName, sourceConnection, dataMartConnection, testID);
            tableCount ++;
        }

        logger.info("Data was loaded successfully!Testid:" + testID );
        logger.info( ++tableCount + " tables and " + ++recordCount + " records were loaded.");
    }

    /**
     * Load the specific table data from source database from data mart
     * @param fieldInfo
     * @param tableName
     * @param sourceConnection
     * @param dataMartConnection
     * @param testID
     * @return the loaded records
     * @throws SQLException
     */
    private int loadOneTableData(Map<String,FieldAttribute>  fieldInfo, String tableName,
                                 Connection sourceConnection, Connection dataMartConnection ,int testID) throws SQLException{
        PreparedStatement ps1 = null ,ps = null ;
        int fieldCount = 0, recordCount=0;
        ResultSet rs = null;
        String testIDName = dataMart.getTestID().getFieldName();
        StringBuilder fieldList = new StringBuilder();
        StringBuilder questionMarkList = new StringBuilder();
        String query,insert,fields[],col;
        FieldAttribute attributes;

        for (Map.Entry<String, FieldAttribute> field : fieldInfo.entrySet()) {
            col = field.getKey();
            if (col.equalsIgnoreCase(testIDName)) continue; //in case the tables in source database has the fields named "testid"
            fieldList.append(col).append(",");
            questionMarkList.append("?,");
        }

        query =  "select " + fieldList.toString() + "1 from " + tableName;
        ps1 = sourceConnection.prepareStatement(query);
        rs = ps1.executeQuery();

        insert = "insert into " + tableName + "(" + fieldList.toString()  + testIDName + ") values(" + questionMarkList + "?)";
        ps = dataMartConnection.prepareStatement(insert);
        fields = fieldList.toString().split(",");

        while (rs.next()){
            for (fieldCount =0; fieldCount<fields.length; fieldCount++){
                col = fields[fieldCount];
                attributes =fieldInfo.get(col);
                if (rs.getObject(col)!=null)
                    attributes.getFieldType().addToSqlPreparedStatement(ps,fieldCount+1,rs.getObject(col));
                else
                    ps.setObject(fieldCount+1,null);
            }
            ps.setInt(++fieldCount, testID);
            ps.executeUpdate();
            recordCount ++;
        }
        DBHandler.releaseDatabaseResource(rs,null,ps,null);
        DBHandler.releaseDatabaseResource(null,null,ps1,null);
        return recordCount;
    }




}
