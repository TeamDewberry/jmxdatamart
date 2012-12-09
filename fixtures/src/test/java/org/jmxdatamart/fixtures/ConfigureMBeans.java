package org.jmxdatamart.fixtures;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import javax.management.*;

/**
 * A FitNesse fixture that configures the attributes of MBeans
 */
public class ConfigureMBeans extends GuicySetUpFixture {

  private final ImmutableMap<String, Function<String, Object>> attributeTypeToObject =
    new ImmutableMap.Builder<String, Function<String, Object>>()
      .put(String.class.getName(), new Function<String, Object>() {
        public Object apply(String stringValue) {
          return stringValue;
        }
      })
      .put(int.class.getName(), new Function<String, Object>() {
        public Object apply(String stringValue) {
          return Integer.parseInt(stringValue);
        }
      })
      .put(long.class.getName(), new Function<String, Object>() {
        public Object apply(String stringValue) {
          return Long.parseLong(stringValue);
        }
      })
      .build();

  @Inject
  private MBeanServer mBeanServer;

  public void beanNameAttributeValue(String beanName, String attribute, String value) throws MalformedObjectNameException, InstanceNotFoundException, ClassNotFoundException, MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IllegalAccessException, InstantiationException, InvalidAttributeValueException, AttributeNotFoundException, ReflectionException, IntrospectionException {
    ObjectInstance bean = getMBean(beanName);
    setAttribute(bean, attribute, value);
  }

  private void setAttribute(ObjectInstance bean, String attributeName, String value) throws InstanceNotFoundException, ReflectionException, AttributeNotFoundException, MBeanException, InvalidAttributeValueException, IntrospectionException {
    ObjectName name = bean.getObjectName();
    setAttribute(name, attributeName, value);
  }

  private void setAttribute(ObjectName name, String attributeName, String stringValue) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IntrospectionException {
    Object value = getValueWithCorrectTypeFor(name, attributeName, stringValue);
    mBeanServer.setAttribute(name, new Attribute(attributeName, value));
  }

  private Object getValueWithCorrectTypeFor(ObjectName name, String attributeName, String stringValue) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
    MBeanAttributeInfo attribute = getAttribute(name, attributeName);
    Function<String, Object> toObject = getStringConverter(attribute.getType());
    return toObject.apply(stringValue);
  }

  private Function<String, Object> getStringConverter(String type) {
    return attributeTypeToObject.get(type);
  }

  private MBeanAttributeInfo getAttribute(ObjectName name, String attributeName) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
    MBeanInfo info = mBeanServer.getMBeanInfo(name);
    MBeanAttributeInfo[] attributes = info.getAttributes();
    for (MBeanAttributeInfo attribute : attributes) {
      if (attribute.getName().equals(attributeName)) {
        return attribute;
      }
    }

    String message = String.format("Cannot find attribute \"%s\" on \"%s\"", attributeName, name);
    throw new IllegalStateException(message);
  }

  private ObjectInstance getMBean(String beanName) throws MalformedObjectNameException, InstanceNotFoundException, ClassNotFoundException, MBeanRegistrationException, InstanceAlreadyExistsException, NotCompliantMBeanException, InstantiationException, IllegalAccessException {
    ObjectName name = new ObjectName(beanName);
    return mBeanServer.getObjectInstance(name);
  }

}
