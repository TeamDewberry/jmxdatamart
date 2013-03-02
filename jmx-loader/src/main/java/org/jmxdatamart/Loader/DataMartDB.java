package org.jmxdatamart.Loader;/*
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

import org.jmxdatamart.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

public class DataMartDB {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String mainTableName = "mainTable";
    private FieldAttribute testID;
    private FieldAttribute importTime ;
    private FieldAttribute importedFile; //use to check if the embedded database has been imported, avoid duplicated import.
    private Setting.DBInfo dbInfo;
    private DBHandler targetDatabase;
    private Properties required, optional;
    private Connection targetConn;

    public Setting.DBInfo getDbInfo() {
        return dbInfo;
    }

    public Properties getRequired() {
        return required;
    }

    public Properties getOptional() {
        return optional;
    }

    public String getMainTableName() {
        return mainTableName;
    }

    public FieldAttribute getTestID() {
        return testID;
    }

    public FieldAttribute getImportTime() {
        return importTime;
    }

    public FieldAttribute getImportedFile() {
        return importedFile;
    }

    public DBHandler getTargetDatabase() {
        return targetDatabase;
    }

    public DataMartDB( Setting.DBInfo dbInfo, Properties re, Properties op){
        this.dbInfo = dbInfo;
        this.required = re;
        this.optional = op;

        testID = new FieldAttribute("testId",DataType.LONG,true);
        importTime = new FieldAttribute("importTime",DataType.DATETIME,false);
        importedFile = new FieldAttribute("importFile",DataType.STRING,false);

        if (dbInfo.getDatabaseType().equalsIgnoreCase("sqlserver")){
            targetDatabase = new MssqlHandler();
            ((MssqlHandler)targetDatabase).setJdbcurl(dbInfo.getJdbcUrl());
        }
        else if (dbInfo.getDatabaseType().equalsIgnoreCase("hsqldb"))
            targetDatabase = new HypersqlHandler();
        else if (dbInfo.getDatabaseType().equalsIgnoreCase("derbydb"))
            targetDatabase = new DerbyHandler();

    }
}
