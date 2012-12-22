package org.jmxdatamart.testwebapp;

public class TestWebAppMBean implements TestWebAppMXBean {

  private final long creationTime = System.currentTimeMillis();
  private int numberOfCalls;

  public long getAge() {
    return System.currentTimeMillis() - creationTime;
  }

  public int getNumberOfCalls() {
    return numberOfCalls++;
  }

}
