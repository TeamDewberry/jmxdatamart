package org.jmxdatamart.fixtures;

import com.google.inject.Inject;
import fitlibrary.SetUpFixture;

import javax.management.*;

/**
 * A FitNesse fixture that configures the attributes of MBeans
 */
public class ConfigureMBeans extends GuicySetUpFixture {

  @Inject
  private MBeanServer mBeanServer;

  public void beanNameAttributeValue(String beanName, String attribute, String value) throws MalformedObjectNameException, InstanceNotFoundException, ClassNotFoundException, MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IllegalAccessException, InstantiationException, InvalidAttributeValueException, AttributeNotFoundException, ReflectionException {
    ObjectInstance bean = getMBean(beanName);
    setAttribute(bean, attribute, value);
  }

  private void setAttribute(ObjectInstance bean, String attributeName, String value) throws InstanceNotFoundException, ReflectionException, AttributeNotFoundException, MBeanException, InvalidAttributeValueException {
    ObjectName name = bean.getObjectName();
    mBeanServer.setAttribute(name, new Attribute(attributeName, value));
  }

  private ObjectInstance getMBean(String beanName) throws MalformedObjectNameException, InstanceNotFoundException, ClassNotFoundException, MBeanRegistrationException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstantiationException, IllegalAccessException {
    ObjectName name = new ObjectName(beanName);
    return mBeanServer.getObjectInstance(name);
  }

}
