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
    BYTE(
            java.lang.Byte.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
        ),      // 8 bit integer
    
    SHORT(
            java.lang.Short.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
         ),     // 16 bit integer
    
    INT(
            java.lang.Integer.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
       ),       // 32 bit integer
    
    LONG(
            java.lang.Long.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
        ),      // 64 bit integer
    
    FLOAT(
            java.lang.Float.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
         ),     // 32 bit single precision
    
    DOUBLE(
            java.lang.Double.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
          ),    // 64 bit double precision
    
    //BOOLEAN,   ms sql doesn't support boolean
    
    CHAR(
            java.lang.Character.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
        ),  // 16 bit UFT-8 character
    
    STRING(
            java.lang.String.class,
            "MS SQL type",
            "Derby type",
            "HSQL type"
          ),    // unlimited-length character sequence type
    
    UNKNOWN(
            null,
            null,
            null,
            null
           )    // internal error type.
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
     * Get the supported type of an object
     * @param obj the object that need checking
     * @return the supported type, or UNKNOWN if not supported
     */
    public boolean isSupported(Object obj) {
        for (DataType d : values()) {
            if (d != UNKNOWN &&
                    d.javaType.isAssignableFrom(obj.getClass())) {
                return true;
            }
        }
        return false;
    }
}
