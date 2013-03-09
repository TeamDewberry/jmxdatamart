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
  public static final Map<String, Car> map = new HashMap<String, Car>();
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
  public void setIndexedStr(String newStr) {
    this.strArr[index] = newStr;
  }

  @Override
  public void setDreamName(String name) {
    this.dream.setName(name);
  }

  @Override
  public void setDreamAutoShow(String name) {
    this.dream.setAutoShow(name);
  }

  @Override
  public void setDreamPower(int power) {
    this.dream.setPower(power);
  }

  @Override
  public void setDreamEngine(int engine) {
    this.dream.setEngine(engine);
  }

  @Override
  public void setIndexedName(String name) {
    this.cars[index].setName(name);
  }

  @Override
  public void setIndexedAutoShow(String autoShow) {
    this.cars[index].setName(autoShow);
  }

  @Override
  public void setIndexedPower(int power) {
    this.cars[index].setPower(power);
  }

  @Override
  public void setIndexedEngine(int engine) {
    this.cars[index].setEngine(engine);
  }
}
