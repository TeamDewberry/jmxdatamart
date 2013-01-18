/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jmxdatamart.Extractor;

import java.util.*;

/**
 * This class contains data related to any MBean such as name, alias and its
 * attributes
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MBeanData implements BeanData {
    private String name;
    private String alias;
    private List<Attribute> attributes;
    private boolean enable;

    /**
     * @return the name
     */
    @Override
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
    @Override
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    
    public MBeanData(String name, String alias, List<Attribute> attributes, boolean enable) {
        this.name = name;
        this.alias = alias;
        this.attributes = attributes;
        this.enable = enable;
    }

    public MBeanData() {
        name = "";
        alias = "";
        enable = true;
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        return  this.name + " -> " + this.alias + nl +
                attributes.toString() + nl;
    }

    /**
     * @return the enable
     */
    @Override
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
