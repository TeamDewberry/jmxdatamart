package org.jmxdatamart;

public interface SimpleMXBean {

  int getIntAttribute();

  void setIntAttribute(int intAttribute);

  String getStringAttribute();

  void setStringAttribute(String stringAttribute);

  long getLongAttribute();

  void setLongAttribute(long longAttribute);
}
