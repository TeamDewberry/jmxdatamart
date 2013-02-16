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
import java.util.Properties;

public class LoaderSetting {

    public class DBInfo {
        String databaseType;
        String jdbcUrl;
        String databaseName;
        Properties userInfo;

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }


        public String getDatabaseType() {
            return databaseType;
        }

        public void setDatabaseType(String databaseType) {
            this.databaseType = databaseType;
        }

        public Properties getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(Properties userInfo) {
            this.userInfo = userInfo;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }
   };

    private DBInfo source, target;
    private Properties required, optional;

    public DBInfo getSource() {
        return source;
    }

    public void setSource(DBInfo source) {
        this.source = source;
    }

    public DBInfo getTarget() {
        return target;
    }

    public void setTarget(DBInfo target) {
        this.target = target;
    }

    public Properties getRequired() {
        return required;
    }

    public void setRequired(Properties required) {
        this.required = required;
    }

    public Properties getOptional() {
        return optional;
    }

    public void setOptional(Properties optional) {
        this.optional = optional;
    }

    public LoaderSetting() {
    }



}

/*

Source.type = Hsqldb
        Source.JDBCurl = "jdbc:hsqldb:file:/wing_homework/dropbox/winter_2013/cs488/jmxdatamart_newfork/HyperSQL/Extrator20130123121454"
        Source.databasename = "jdbc:hsqldb:file:/wing_homework/dropbox/winter_2013/cs488/jmxdatamart_newfork/HyperSQL/Extrator20130123121454"
        Source.user = "sa"
        Source.password = "whatever*/
