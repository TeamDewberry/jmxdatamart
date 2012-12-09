package org.jmxdatamart;

public class SimpleMBean implements SimpleMXBean {

  private String stringAttribute;
  private int intAttribute;
  private long longAttribute;

  public String getStringAttribute() {
    return stringAttribute;
  }

  public void setStringAttribute(String stringAttribute) {
    this.stringAttribute = stringAttribute;
  }

  public int getIntAttribute() {
    return intAttribute;
  }

  public void setIntAttribute(int intAttribute) {
    this.intAttribute = intAttribute;
  }

  public long getLongAttribute() {
    return longAttribute;
  }

  public void setLongAttribute(long longAttribute) {
    this.longAttribute = longAttribute;
  }
}
