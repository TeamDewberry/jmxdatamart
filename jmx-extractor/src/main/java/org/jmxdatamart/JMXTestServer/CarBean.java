/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.JMXTestServer;

import java.util.HashMap;
import java.util.Map;
import org.jmxdatamart.Extractor.Attribute;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CarBean implements CarMXBean{
    private Car dream;
    
    public static final String NAME = "2014 Chevy Corvette Stingray";
    public static final String YEAR = "2013 Detroit auto show";
    public static final int ENGINE = 8;
    public static final int POWER = 450;
    public static final Map<String, Car> map = new HashMap<String, Car>();
    
    public static final int[] intArr = {1,2,3,5,8,13,21};
    public static final String[] strArr = new String[intArr.length];
    
    public CarBean() {
        dream = new Car(NAME , YEAR, ENGINE, POWER);
        map.put("Car", dream);
        for (int i = 0; i < intArr.length; ++i) {
          strArr[i] = "#" + intArr[i];
        }
    }

    @Override
    public Car getCar() {
        return dream;
    }

    @Override
    public Map<String, Car> getMap() {
        return map;
    }

  @Override
  public int[] getIntList() {
    return intArr;
  }

  @Override
  public String[] getStrList() {
    return strArr;
  }
}
