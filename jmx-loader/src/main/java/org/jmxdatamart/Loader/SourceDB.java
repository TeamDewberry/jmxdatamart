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

import java.io.File;
import java.util.*;

public class SourceDB {
    private Set<String> databaseFiles;
    private DBHandler sourceDatabase;
    private Setting.DBInfo dbInfo;

    public DBHandler getSourceDatabase() {
        return sourceDatabase;
    }
    public Setting.DBInfo getDbInfo() {
        return dbInfo;
    }

    public Set<String> getDatabaseFiles() {
        return databaseFiles;
    }

    public SourceDB(Setting.DBInfo dbInfo, File folderLocation) {
        this.dbInfo = dbInfo;
        if (dbInfo.getDatabaseType().equals(DataType.SupportedDatabase.HSQL))
            sourceDatabase = new HypersqlHandler();
        else if (dbInfo.getDatabaseType().equals(DataType.SupportedDatabase.DERBY))
            sourceDatabase = new DerbyHandler();
        else
            throw new RuntimeException("Doesn't support this source database type");

        databaseFiles = new TreeSet<String>();
        String fileName, databaseName;
        for (final File fileEntry : folderLocation.listFiles()) {
            if (fileEntry.isFile()) {
                fileName= fileEntry.getName();
                databaseName = fileName.split("\\.")[0];
                if (databaseName.trim().length()>0)
                    if ((fileName.split("\\.").length==2) && fileName.split("\\.")[1].equalsIgnoreCase("script"))
                        databaseFiles.add(folderLocation.getAbsolutePath()+"/"+databaseName);
            }
        }
    }

}
