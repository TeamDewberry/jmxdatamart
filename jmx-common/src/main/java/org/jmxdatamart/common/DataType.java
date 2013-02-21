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
            "SMALLINT", // 8bit data type in T-SQL is only 0-255
            "SMALLINT", // Derby doesnt support 1Byte 
            "TINYINT" // -127 to 128 like Java
        )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setByte(index, ((Byte)value).byteValue());
            }
        }
    },      
    
    SHORT   // 16 bit integer
         (
            java.lang.Short.class,
            "SMALLINT",
            "SMALLINT",
            "SMALLINT"
         )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setShort(index, ((Short)value).shortValue());
            }
        }
    },     
    
    INT     // 32 bit integer
       (
            java.lang.Integer.class,
            "INT",
            "INT",
            "INTEGER"
       )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setInt(index, ((Integer)value).intValue());
            }
        }
    },       
    
    LONG    // 64 bit integer
        (
            java.lang.Long.class,
            "BIGINT",
            "BIGINT",
            "BIGINT"
        )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setLong(index, ((Long)value).longValue());
            }
        }
    },      
    
    FLOAT   // 32 bit single precision
         (
            java.lang.Float.class,
            "REAL", //T-SQL does not conform to standards but this is close 4Bytes
            "REAL", //Derby has different limits than Java 4Bytes
            "DOUBLE" //"REAL, FLOAT and DOUBLE values are all stored in the database as java.lang.Double objects"
         )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setFloat(index, ((Float)value).floatValue());
            }
        }
    },     
    
    DOUBLE  // 64 bit double precision
          (
            java.lang.Double.class,
            "FLOAT(53)", //T-SQL does not conform to standards but this is close 8Bytes
            "DOUBLE",  //Derby limits are different than Java 8Bytes
            "DOUBLE" 
          )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setDouble(index, ((Double)value).doubleValue());
            }
        }
    },    
    
    //BOOLEAN,   ms sql doesn't support boolean
    
    CHAR    // 16 bit UFT-8 character
        (
            java.lang.Character.class,
            "NCHAR(1)", 
            "CHAR(1)",
            "CHAR(1)"
        )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
                ps.setString(index, value.toString());
            }
        }
    },  
    
    STRING  // unlimited-length character sequence type
          (
            java.lang.String.class,
            "NVARCHAR(MAX)",
            "VARCHAR (32672)", // Derby max is Integer.Max_Value, not padded
            "LONGVARCHAR"
          )
    {
        @Override
        public void addToSqlPreparedStatement(PreparedStatement ps, int index, Object value) throws SQLException {
            if (checkTypeOf(value)) {
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
        public boolean checkTypeOf(Object obj) {
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
    public boolean checkTypeOf(Object obj) {
        return javaType.isAssignableFrom(obj.getClass());
    }
    
    public abstract void addToSqlPreparedStatement(
            PreparedStatement ps,
            int index,
            Object value) throws SQLException;
}
