package org.jmxdatamart.fixtures;

import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class CheckMBeanRegistration extends GuicyColumnFixture {

  public String beanName;

  @Inject
  private MBeanServer mBeanServer;

  public boolean registered() throws MalformedObjectNameException {
    return mBeanServer.isRegistered(new ObjectName(beanName));
  }
}
