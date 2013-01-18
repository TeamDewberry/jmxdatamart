/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

/**
 * This class contains information related to any given attribute such as name,
 * alias and its data type
 *
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.dataType != null ? this.dataType.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.alias != null ? this.alias.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Attribute other = (Attribute) obj;
        if (this.dataType != other.dataType) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.alias == null) ? (other.alias != null) : !this.alias.equals(other.alias)) {
            return false;
        }
        return true;
    }
}
