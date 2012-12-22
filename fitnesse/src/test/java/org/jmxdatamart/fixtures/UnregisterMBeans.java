package org.jmxdatamart.fixtures;

import com.google.inject.Inject;

import javax.management.*;

public class UnregisterMBeans extends GuicySetUpFixture {

  @Inject
  private MBeanServer mBeanServer;

  public void beanName(String beanName) throws MalformedObjectNameException, InstanceNotFoundException, MBeanRegistrationException {
    ObjectName query = new ObjectName(beanName);
    for (ObjectName name : mBeanServer.queryNames(query, null)) {
      mBeanServer.unregisterMBean(name);
    }
  }
}
