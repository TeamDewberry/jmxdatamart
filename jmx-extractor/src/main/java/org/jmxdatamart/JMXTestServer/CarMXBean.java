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
    
    public Car[] getCarList();
    
    public void setIndex(int newIndex);
    
    public void setIndexedInt(int newInt);
    
    public void setIndexedStr(String newStr);
    
    public void setDreamName(String name);
    
    public void setDreamAutoShow(String name);
    
    public void setDreamPower(int power);
    
    public void setDreamEngine(int engine);
    
    public void setIndexedName(String name);
    
    public void setIndexedAutoShow(String autoShow);
    
    public void setIndexedPower(int power);
    
    public void setIndexedEngine(int engine);
}
