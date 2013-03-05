/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.JMXTestServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final Car[] cars = new Car[3];
    
    public static final int[] intArr = {1,2,3,5,8,13,21};
    public static final String[] strArr = new String[intArr.length];
    
    public CarBean() {
        dream = new Car(NAME , YEAR, ENGINE, POWER);
        
        for (int i = 0; i < intArr.length; ++i) {
          strArr[i] = "#" + intArr[i];
        }
        
        cars[0] = dream;
        cars[1] = new Car("2014 Audi RS7 ", "2013 Detroit auto show", 8, 600);
        cars[2] = new Car("Destino", "2013 Detroit auto show", 8, 638);
        
        map.put("Stingray", cars[0]);
        map.put("Audi", cars[1]);
        map.put("VL Automotive", cars[2]);
    }
    
    public CarBean(String name, String year, int engine, int power) {
      dream = new Car(name, year, engine, power);
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

  @Override
  public Car[] getCarList() {
    return cars;
  }
}
