/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.JMXTestServer;

import java.util.Map;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public interface CarMXBean {
    public Car getCar();
    
    public Map<String, Car> getMap();
    
    public int[] getIntList();
    
    public String[] getStrList();
}
