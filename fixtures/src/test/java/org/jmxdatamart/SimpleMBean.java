package org.jmxdatamart;

public class SimpleMBean implements SimpleMXBean {

  private String stringAttribute;
  private int intAttribute;

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

}
