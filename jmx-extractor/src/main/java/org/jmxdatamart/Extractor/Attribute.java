/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jmxdatamart.Extractor;

/**
 * This class contains information related to any given attribute such as 
 * name, alias and its data type
 * @author Binh Tran <mynameisbinh@gmail.com>
 */

public class Attribute {
    private DataType dataType;
    private String name;
    private String alias;

    public Attribute() {
        name = "";
        alias = "";
    }
    
    /**
     * @return the dataType
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public Attribute(String name, String alias, DataType dataType) {
        this.name = name;
        this.alias = alias;
        this.dataType = dataType;
    }
    
    @Override
    public String toString() {
        return dataType.toString() + ": " + name + " -> " + alias;
    }
}
