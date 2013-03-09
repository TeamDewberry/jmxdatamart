package org.jmxdatamart.JMXTestServer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CarBean implements CarMXBean {

  private Car dream;
  private int index;
  private String key;
  public final Map<String, Car> map = new HashMap<String, Car>();
  public final Car[] cars = new Car[7];
  public final int[] intArr = {1, 2, 3, 5, 8, 13, 21};
  public final String[] strArr = new String[intArr.length];

  public CarBean() {
    dream = new Car();

    for (int i = 0; i < intArr.length; ++i) {
      strArr[i] = "#" + intArr[i];
    }

    cars[0] = dream;
    cars[1] = new Car("2014 Audi RS7 ", "2013 Detroit", 8, 600);
    cars[2] = new Car("Destino", "2013 Detroit", 8, 638);
    cars[3] = new Car();
    cars[4] = new Car();
    cars[5] = new Car();
    cars[6] = new Car();

    map.put("Stingray", cars[0]);
    map.put("Audi", cars[1]);
    map.put("VL Automotive", cars[2]);
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

  public void setCarList(int index, String name, String year, int engine, int power) {
    cars[index] = new Car(name, year, engine, power);
  }

  @Override
  public void setIndex(int index) {
    this.index = index;
  }

  @Override
  public void setIndexedInt(int newInt) {
    this.intArr[index] = newInt;
  }

  @Override
  public void setIndexedString(String newStr) {
    this.strArr[index] = newStr;
  }

  @Override
  public void setCarName(String name) {
    this.dream.setName(name);
  }

  @Override
  public void setCarAutoShow(String name) {
    this.dream.setAutoShow(name);
  }

  @Override
  public void setCarPower(int power) {
    this.dream.setPower(power);
  }

  @Override
  public void setCarEngine(int engine) {
    this.dream.setEngine(engine);
  }

  @Override
  public void setIndexedCarName(String name) {
    this.cars[index].setName(name);
  }

  @Override
  public void setIndexedCarAutoShow(String autoShow) {
    this.cars[index].setName(autoShow);
  }

  @Override
  public void setIndexedCarPower(int power) {
    this.cars[index].setPower(power);
  }

  @Override
  public void setIndexedCarEngine(int engine) {
    this.cars[index].setEngine(engine);
  }

  @Override
  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public void setKeyedName(String name) {
    if (!this.map.containsKey(key)) {
    this.map.put(key, new Car());
    }
    this.map.get(key).setName(name);
  }

  @Override
  public void setKeyedAutoShow(String autoShow) {
    if (!this.map.containsKey(key)) {
      this.map.put(key, new Car());
    }
    this.map.get(key).setAutoShow(autoShow);
  }

  @Override
  public void setKeyedPower(int power) {
    if (!this.map.containsKey(key)) {
      this.map.put(key, new Car());
    }
    this.map.get(key).setPower(power);
  }

  @Override
  public void setKeyedEngine(int engine) {
    if (!this.map.containsKey(key)) {
      this.map.put(key, new Car());
    }
    this.map.get(key).setEngine(engine);
  }
}
