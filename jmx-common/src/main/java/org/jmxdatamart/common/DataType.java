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

/**
 * Supported data types
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public enum DataType {
    BYTE    // 8 bit integer
            (
                    java.lang.Byte.class,
                    java.sql.Types.SMALLINT,
                    "SMALLINT", // 8bit data type in T-SQL is only 0-255
                    "SMALLINT", // Derby doesnt support 1Byte
                    "TINYINT" // -127 to 128 like Java
            ),

    SHORT   // 16 bit integer
            (
                    java.lang.Short.class,
                    java.sql.Types.SMALLINT,
                    "SMALLINT",
                    "SMALLINT",
                    "SMALLINT"
            ),

    INT     // 32 bit integer
            (
                    java.lang.Integer.class,
                    java.sql.Types.INTEGER,
                    "INT",
                    "INT",
                    "INTEGER"
            ),

    LONG    // 64 bit integer
            (
                    java.lang.Long.class,
                    java.sql.Types.BIGINT,
                    "BIGINT",
                    "BIGINT",
                    "BIGINT"
            ),

    FLOAT   // 32 bit single precision
            (
                    java.lang.Float.class,
                    java.sql.Types.FLOAT,
                    "REAL", //T-SQL does not conform to standards but this is close 4Bytes
                    "REAL", //Derby has different limits than Java 4Bytes
                    "DOUBLE" //"REAL, FLOAT and DOUBLE values are all stored in the database as java.lang.Double objects"
            ),

    DOUBLE  // 64 bit double precision
            (
                    java.lang.Double.class,
                    java.sql.Types.DOUBLE,
                    "FLOAT(53)", //T-SQL does not conform to standards but this is close 8Bytes
                    "DOUBLE",  //Derby limits are different than Java 8Bytes
                    "DOUBLE"
            ),

    BOOLEAN   // boolean variable
            (
                    java.lang.Boolean.class,
                    java.sql.Types.BOOLEAN,
                    "BIT",
                    "BOOLEAN",
                    "BOOLEAN"
            ),   

    CHAR    // 16 bit UFT-8 character
            (
                    java.lang.Character.class,
                    java.sql.Types.CHAR,
                    "NCHAR(1)",
                    "CHAR(1)",
                    "CHAR(1)"
            ),

    STRING  // unlimited-length character sequence type
            (
                    java.lang.String.class,
                    java.sql.Types.VARCHAR,
                    "NVARCHAR(MAX)",
                    "VARCHAR (32672)", // Derby max is Integer.Max_Value, not padded
                    "LONGVARCHAR"
            ),
    DATETIME(   // date type
                java.util.Date.class,
                java.sql.Types.TIMESTAMP,
                "datetime",
                "timestamp",
                "timestamp"
            )
            {
                @Override
                public void addToSqlPreparedStatement(java.sql.PreparedStatement ps, int index, Object value) throws java.sql.SQLException {
                  if (java.util.Date.class.isAssignableFrom(value.getClass())) {
                    java.util.Date d = (java.util.Date)value;
                    java.sql.Timestamp ts = new java.sql.Timestamp(d.getTime());
                    ps.setTimestamp(index, ts);
                  }
                  }
            },
    UNKNOWN(    // internal error type.
            null,
            -9999,
            null,
            null,
            null
    )
            {
                @Override
                public void addToSqlPreparedStatement(java.sql.PreparedStatement ps, int index, Object value) {
                    throw new UnsupportedOperationException("Type UNKNOWN doesn't support this operation");
                }
                @Override
                public boolean supportsTypeOf(Object obj) {
                    return false;
                }
                @Override
                public String toString(Object obj) {
                    return toString();
                }
            }    
    ;

    private  Class javaType;
    private  int jdbcTypeID;
    private  String mssqlType;
    private  String derbyType;
    private  String hsqlType;

    private DataType(Class javaType, int jdbcTypeID, String mssqlType, String derbyType, String hsqlType) {
        this.javaType = javaType;
        this.jdbcTypeID = jdbcTypeID;
        this.mssqlType = mssqlType;
        this.derbyType = derbyType;
        this.hsqlType = hsqlType;
    }

    public String getDerbyType() {
        return derbyType;
    }

    public String getHsqlType() {
        return hsqlType;
    }

    public String getMssqlType() {
        return mssqlType;
    }

    public int getJdbcTypeID() {

        return jdbcTypeID;
    }

    public Class getJavaType() {
        return javaType;
    }
    
    public enum SupportedDatabase {
      MSSQL, HSQL, DERBY, JDBCID, JAVACLASS
    }
    
    public Object getType(SupportedDatabase type) {
      switch(type) {
        case MSSQL:
          return getMssqlType();
        case HSQL:
          return getHsqlType();
        case DERBY:
          return getDerbyType();
        case JDBCID:
          return getJdbcTypeID();
        case JAVACLASS:
          return getJavaType();
        default:
          return null;
      }
    }

    public Object getType(String type){
        if (type.equalsIgnoreCase("sqlserver")){
            return getMssqlType();
        }
        else if (type.equalsIgnoreCase("hsqldb")){
            return getHsqlType();
        }
        else if (type.equalsIgnoreCase("derbydb")){
            return getDerbyType();
        }
        else if (type.equalsIgnoreCase("id")){
            return getJdbcTypeID();
        }
        else{
            return getJavaType();
        }
    }

    public static DataType findCorrespondDataTypeByID(int currentTypeID ) {
        for (DataType type:DataType.values()) {
            if ((type.getType("id")).equals(Integer.valueOf(currentTypeID))){
                return type;
            }
        }
        return null;
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
    public boolean supportsTypeOf(Object obj) {
        return javaType.isAssignableFrom(obj.getClass());
    }
    
    public static DataType getDataType(Object obj) {
      for (DataType d : values()) {
        if (d.supportsTypeOf(obj)) {
          return d;
        }
      }
      return UNKNOWN;
    }
    
    public void addToSqlPreparedStatement(
            java.sql.PreparedStatement ps,
            int index,
            Object value) throws java.sql.SQLException
    {
      ps.setObject(index, value, jdbcTypeID);
    }
}
