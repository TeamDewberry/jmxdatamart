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
    
    public void setIndexedString(String newStr);
    
    public void setCarName(String name);
    
    public void setCarAutoShow(String name);
    
    public void setCarPower(int power);
    
    public void setCarEngine(int engine);
    
    public void setIndexedCarName(String name);
    
    public void setIndexedCarAutoShow(String autoShow);
    
    public void setIndexedCarPower(int power);
    
    public void setIndexedCarEngine(int engine);
    
    public void setKey(String key);
    
    public void setKeyedName(String name);
    
    public void setKeyedAutoShow(String autoShow);
    
    public void setKeyedPower(int power);
    
    public void setKeyedEngine(int engine);
}
