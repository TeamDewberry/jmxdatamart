/*
 * Copyright (c) 2012, Tripwire, Inc.
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

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Supported data types
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public enum DataType {
    BYTE    // 8 bit integer
        (
            java.lang.Byte.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
        )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setByte(index, ((Byte)value).byteValue());
            }
        }
    },      
    
    SHORT   // 16 bit integer
         (
            java.lang.Short.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
         )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setShort(index, ((Short)value).shortValue());
            }
        }
    },     
    
    INT     // 32 bit integer
       (
            java.lang.Integer.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
       )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setInt(index, ((Integer)value).intValue());
            }
        }
    },       
    
    LONG    // 64 bit integer
        (
            java.lang.Long.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
        )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setLong(index, ((Long)value).longValue());
            }
        }
    },      
    
    FLOAT   // 32 bit single precision
         (
            java.lang.Float.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
         )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setFloat(index, ((Float)value).floatValue());
            }
        }
    },     
    
    DOUBLE  // 64 bit double precision
          (
            java.lang.Double.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
          )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setDouble(index, ((Double)value).doubleValue());
            }
        }
    },    
    
    //BOOLEAN,   ms sql doesn't support boolean
    
    CHAR    // 16 bit UFT-8 character
        (
            java.lang.Character.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
        )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setString(index, value.toString());
            }
        }
    },  
    
    STRING  // unlimited-length character sequence type
          (
            java.lang.String.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
          )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (supportTypeOf(value)) {
                ps.setString(index, value.toString());
            }
        }
    },    
    
    UNKNOWN(
            null,
            null,
            null,
            null
           )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) {
            throw new UnsupportedOperationException("Type UNKNOWN doesn't support this operation");
        }
        @Override
        public boolean supportTypeOf(Object obj) {
            return false;
        } 
        @Override
        public String toString(Object obj) {
            return toString();
        }
    }    // internal error type.
    ;
    
    private final Class javaType;
    private final String mssqlType;
    private final String derbyType;
    private final String hsqlType;

    private DataType(Class javaType, String mssqlType, String derbyType, String hsqlType) {
        this.javaType = javaType;
        this.mssqlType = mssqlType;
        this.derbyType = derbyType;
        this.hsqlType = hsqlType;
    }
    
    public String getMsSqlType() {
        return mssqlType;
    }
    
    public String getDerbyType() {
        return derbyType;
    }

    public String getHsqlType() {
        return hsqlType;
    }
    
    public Class getJavaType() {
        return javaType;
    }
    
    /**
     * This function return the value of supported argument type as string, and
     * throws IllegalArgumentException if doesn't supported that type. 
     * @param obj the object that need to obtain the value
     * @return string of corresponding value
     */
    public String toString(Object obj) {
        if (this.javaType.isAssignableFrom(obj.getClass())) {
            return obj.toString();      // doesn't need class because already have isAssignableFrom guard
        } else {
            throw new IllegalArgumentException("Does not support " + obj.getClass().toString());
        }
    }
    
    /**
     * Check if obj has the compatible type
     * @param obj obj whose type is to be checked
     * @return true if obj has compatible type, otherwise false
     */
    public boolean supportTypeOf(Object obj) {
        return javaType.isAssignableFrom(obj.getClass());
    }
    
    public abstract void addToSqlPreparedStatement(
            PreparedStatement ps,
            int index,
            Object value) throws SQLException;
}
