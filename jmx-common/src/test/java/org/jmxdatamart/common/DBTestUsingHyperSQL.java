package org.jmxdatamart.common;
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


import java.sql.*;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;
import static junit.framework.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.*;
import org.jmxdatamart.common.*;

public class DBTestUsingHyperSQL {

    private DBHandler db = new HypersqlHandler();
    private Properties p = new Properties();
    String dbname = "HyperSql/JunitTestDB" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    String tablename = "mainTable";
    FieldAttribute newcol = new FieldAttribute("testcol", DataType.LONG, false);
    DataType.SupportedDatabase databaseType= DataType.SupportedDatabase.MSSQL;

    public DBTestUsingHyperSQL(){
        p.put("username","sa");
        p.put("password","whatever");
    }

    @Test
    public void testDBOperation(){

        assertTrue(db.connectServer(p));
        DBHandler ms = new MssqlHandler();
        assertFalse(ms.connectServer(p));

        assertFalse(db.databaseExists(dbname, p));

        Connection conn = null;
        try{
            DBHandler.checkConnection(conn);
        }
        catch (DBException se){
           assertEquals("Can't connect to database.", se.getMessage());
        }
        catch(SQLException se){
            assertEquals("Can't connect to database.", se.getMessage());
        }


        conn= db.connectDatabase(dbname,p);
        assertTrue(db.databaseExists(dbname,p));
        try{
            DBHandler.checkConnection(conn);
        }
        catch (DBException se){
            fail("Connection should valid.");
        }
        catch(SQLException se){
            assertEquals("Can't connect to database.", se.getMessage());
        }

        assertFalse(DBHandler.tableExists(tablename, conn));

        DBHandler.addTable(conn, tablename, databaseType);
        assertTrue(DBHandler.columnExists("autoid", tablename, conn));
        assertFalse(DBHandler.columnExists("testcol", tablename, conn));
        assertTrue(DBHandler.tableExists(tablename,conn));


        DBHandler.addColumn(conn,tablename, newcol,databaseType);
        assertTrue(DBHandler.columnExists("autoid",tablename,conn));
        assertTrue(DBHandler.columnExists("testcol",tablename,conn));

        Map<String,Map> s = DBHandler.getDatabaseSchema(conn,db.getTableSchema(),databaseType);
        assertTrue(s.containsKey(tablename.toUpperCase()));

        Map<String,FieldAttribute> f = s.get(tablename.toUpperCase());
        assertTrue(f.containsKey("testcol".toUpperCase()));

        ((HypersqlHandler)db).shutdownDatabase(conn);
        DBHandler.releaseDatabaseResource(null,null,null,conn);


    }


}
