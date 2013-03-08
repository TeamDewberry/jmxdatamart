package org.jmxdatamart.JMXTestServer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CarBean implements CarMXBean{
    private Car dream;
    private int index;
    private Car tempCar;
    
    public static final String NAME = "2014 Chevy Corvette Stingray";
    public static final String YEAR = "2013 Detroit auto show";
    public static final int ENGINE = 8;
    public static final int POWER = 450;
    public static final Map<String, Car> map = new HashMap<String, Car>();
    public static final Car[] cars = new Car[3];
    
    public static final int[] intArr = {1,2,3,5,8,13,21};
    public static final String[] strArr = new String[intArr.length];
    public static final String[] strArr2 = new String[50];
    
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
  
  public void setIntList(int index, int value) {
    intArr[index] = value;
  }

  @Override
  public String[] getStrList() {
    return strArr;
  }
  
  public void setStrList(int index, String value) {
    strArr[index] = value;
  }

  @Override
  public Car[] getCarList() {
    return cars;
  }
  
  public void setIndex(int index){
      this.index = index;
  }
  
  public void setStrArr(String s){
      strArr2[index] = s;
  }
  
  public void setCar(Car car){
      tempCar = car;
  }
  
  public void setCarName(String name){
      tempCar.setName(name);
  }
  
  public void setCarYear(String year){
      tempCar.setYear(year);
  }
  
  public void setCarEngine(int eng){
      tempCar.setEngine(eng);
      
  }
  
  public void setCarPower(int pow){
      tempCar.setPower(pow);
  }
  
  public void setCarList(int index, String name, String year, int engine, int power) {
    cars[index] = new Car(name, year, engine, power);
  }
}
