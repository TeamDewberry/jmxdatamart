package org.jmxdatamart.fixtures;

import fitlibrary.SetUpFixture;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class CreateMBeans extends SetUpFixture {

  private MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

  public void beanClassBeanName(String beanClass, String beanName) throws MalformedObjectNameException, MBeanRegistrationException, InstanceAlreadyExistsException, ClassNotFoundException, NotCompliantMBeanException, InstantiationException, IllegalAccessException {
    ObjectName name = new ObjectName(beanName);
    createAndRegisterMBean(beanClass, name);
  }

  private void createAndRegisterMBean(String beanClassName, ObjectName name) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MBeanRegistrationException, InstanceAlreadyExistsException, NotCompliantMBeanException {
    Class beanClass = Class.forName(beanClassName);
    mBeanServer.registerMBean(beanClass.newInstance(), name);
  }



}
