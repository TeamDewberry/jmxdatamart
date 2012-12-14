package org.jmxdatamart.fixtures;

import com.google.inject.Inject;
import fit.ColumnFixture;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * A FitNesse fixture that validates that MBean attributes have the expected values
 */
public class ValidateMBeans extends GuicyColumnFixture {

  String beanName;
  String attribute;

  @Inject
  MBeanServer mBeanServer;

  public String value() throws MalformedObjectNameException, InstanceNotFoundException, ReflectionException, AttributeNotFoundException, MBeanException {
    ObjectName name = new ObjectName(beanName);
    return String.valueOf(mBeanServer.getAttribute(name, attribute));
  }

}
