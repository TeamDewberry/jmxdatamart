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

package org.jmxdatamart.Extractor.Setting;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MalformedObjectNameException;
import org.jmxdatamart.common.DataType;
import org.slf4j.LoggerFactory;



/**
 * This class contains settings for the extractor : polling rate, output file(s)
 * location, JMX server URL and what bean to collect.
 * @author Binh Tran <mynameisbinh@gmail.com>
 */

public class Settings {

    private long pollingRate;
    private String folderLocation;
    private String url;
    private List<MBeanData> beans;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.pollingRate ^ (this.pollingRate >>> 32));
        hash = 67 * hash + (this.folderLocation != null ? this.folderLocation.hashCode() : 0);
        hash = 67 * hash + (this.url != null ? this.url.hashCode() : 0);
        hash = 67 * hash + (this.beans != null ? this.beans.hashCode() : 0);
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
        final Settings other = (Settings) obj;
        if (this.pollingRate != other.pollingRate) {
            return false;
        }
        if ((this.folderLocation == null) ? (other.folderLocation != null) : !this.folderLocation.equals(other.folderLocation)) {
            return false;
        }
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        if (this.beans != other.beans && (this.beans == null || !this.beans.equals(other.beans))) {
            return false;
        }
        return true;
    }
    
    
    /**
     * @return the pollingRate
     */
    public long getPollingRate() {
        return pollingRate;
    }

    /**
     * @param pollingRate the pollingRate to set
     */
    public void setPollingRate(long pollingRate) {
        this.pollingRate = pollingRate;
    }

    /**
     * @return the folderLocation
     */
    public String getFolderLocation() {
        return folderLocation;
    }

    /**
     * @param folderLocation the folderLocation to set
     */
    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the beans
     */
    public List<MBeanData> getBeans() {
        return beans;
    }

    /**
     * @param beans the beans to set
     */
    public void setBeans(List<MBeanData> beans) {
        this.beans = beans;
    }
    
    public Settings() {
        XStream xstream = new XStream(new DomDriver());
        xstream.aliasField("BeanList", Settings.class, "beans");
        xstream.aliasField("AttributeList", MBeanData.class, "attributes");
        xstream.alias("Settings", Settings.class);
        xstream.alias("Bean", MBeanData.class);
        xstream.alias("Attribute", Attribute.class);
    }
    
    /**
     * Check if the setting is well formatted.
     * @return true if setting is well formatted, false if not
     * @throws MalformedObjectNameException if a MBean name in setting is malformated
     */
    public boolean check() throws MalformedObjectNameException {
        for (MBeanData mbd : this.beans) {
            if (!mbd.check()) {
                return false;
            }
            mbd.isPattern();
        }
        return true;
    }
    
    public String toXML() {
        XStream xstream = new XStream(new DomDriver());
        xstream.aliasField("BeanList", Settings.class, "beans");
        xstream.aliasField("AttributeList", MBeanData.class, "attributes");
        xstream.alias("Settings", Settings.class);
        xstream.alias("Bean", MBeanData.class);
        xstream.alias("Attribute", Attribute.class);
        
        return xstream.toXML(this);
    }
    
    public static Settings fromXML(String s) {
        XStream xstream = new XStream(new DomDriver());
        xstream.aliasField("BeanList", Settings.class, "beans");
        xstream.aliasField("AttributeList", MBeanData.class, "attributes");
        xstream.alias("Settings", Settings.class);
        xstream.alias("Bean", MBeanData.class);
        xstream.alias("Attribute", Attribute.class);
        
        Settings settings = (Settings)xstream.fromXML(s);
        try {
            settings.check();
        } catch (MalformedObjectNameException ex) {
            LoggerFactory.getLogger(Settings.class).error("Setting is malformated", ex);
            throw new RuntimeException(ex);
        }
        return settings;
        
    }
    
    public static Settings fromXML(InputStream s) {
        XStream xstream = new XStream(new DomDriver());
        xstream.aliasField("BeanList", Settings.class, "beans");
        xstream.aliasField("AttributeList", MBeanData.class, "attributes");
        xstream.alias("Settings", Settings.class);
        xstream.alias("Bean", MBeanData.class);
        xstream.alias("Attribute", Attribute.class);

        Settings settings = (Settings)xstream.fromXML(s);
        try {
            settings.check();
        } catch (MalformedObjectNameException ex) {
            LoggerFactory.getLogger(Settings.class).error("Setting is malformated", ex);
            throw new RuntimeException(ex);
        }
        return settings;
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        return  "Rate = " + pollingRate + nl +
                "Loc = " + folderLocation + nl +
                "URL = " + url + nl +
                beans.toString();
        
    }
    
//    public static void main( String[] args ) throws IOException, MalformedObjectNameException
//    {
//        //Test reading file
//        Settings s1 = Settings.fromXML(new FileInputStream("Settings.xml"));
//        System.out.println(s1.toString());
//        System.out.println("Read xml settings complete");
//        
//        Settings s = new Settings();
//        s.setFolderLocation("\\project\\");
//        s.setPollingRate(5);
//        s.setUrl("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
//        s.setBeans(new ArrayList<MBeanData>());
//        
//        MBeanData bd = new MBeanData("com.example:type=Hello","", new ArrayList<Attribute>(), true);
//        bd.getAttributes().add(new Attribute("Name", "", DataType.STRING));
//        bd.getAttributes().add(new Attribute("CacheSize", "", DataType.INT));
//        s.getBeans().add(bd);
//        s.check();
//        System.out.println(s.toString());
//        
//        String sXML = s.toXML();
//        System.out.println(sXML);
//        FileWriter out = new FileWriter("settings.xml");
//        out.write(sXML);
//        out.close();
//    }
}