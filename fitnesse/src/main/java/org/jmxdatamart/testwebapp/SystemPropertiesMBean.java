/*
 * Copyright (c) 2013, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jmxdatamart.testwebapp;

import com.google.common.collect.Lists;

import javax.management.*;
import java.util.*;

/**
 * A dynamic MBean that provides access to the JVM's System properties
 */
public class SystemPropertiesMBean implements DynamicMBean {
  private final Properties properties = System.getProperties();

  @Override
  public Object getAttribute(String key) throws AttributeNotFoundException, MBeanException, ReflectionException {
    return getProperty(key);
  }

  private Object getProperty(String key) {
    String value = properties.getProperty(key);
    if (isInteger(value)) {
      return Integer.valueOf(value);

    } else if (isBoolean(value)) {
      return Boolean.valueOf(value);

    } else {
      return value;
    }
  }

  private boolean isBoolean(String value) {
    return "true".equals(value) || "false".equals(value);
  }

  private boolean isInteger(String value) {
    try {
      Integer.parseInt(value);
      return true;

    } catch (NumberFormatException ex) {
      return false;
    }
  }

  @Override
  public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
    setProperty(attribute);
  }

  private void setProperty(Attribute attribute) {
    properties.setProperty(attribute.getName(), String.valueOf(attribute.getValue()));
  }

  @Override
  public AttributeList getAttributes(String[] names) {
    AttributeList attributes = new AttributeList(names.length);

    for (String name : names) {
      Object value = getProperty(name);
      if (value != null) {
        attributes.add(new Attribute(name, value));
      }
    }

    return attributes;
  }

  @Override
  public AttributeList setAttributes(AttributeList attributes) {
    for (Attribute attribute : attributes.asList()) {
      setProperty(attribute);
    }

    return attributes;
  }

  @Override
  public Object invoke(String name, Object[] args, String[] signature) throws MBeanException, ReflectionException {
    throw new ReflectionException(new NoSuchMethodException(name));
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    return new MBeanInfo(this.getClass().getName(), "JVM System Properties", getMBeanAttributeInfos(), null, null, null);
  }

  private MBeanAttributeInfo[] getMBeanAttributeInfos() {
    SortedMap<String, String> properties = sortProperties();
    List<MBeanAttributeInfo> infos = Lists.newArrayList();

    for (String key : properties.keySet()) {
      infos.add(getMBeanAttributeInfo(key));
    }

    return infos.toArray(new MBeanAttributeInfo[infos.size()]);
  }

  private MBeanAttributeInfo getMBeanAttributeInfo(String key) {
    Class<?> valueType = getProperty(key).getClass();
    return new MBeanAttributeInfo(key, valueType.getName(), "System Property " + key, true, true, false);
  }

  @SuppressWarnings("unchecked")
  private SortedMap<String, String> sortProperties() {
    return new TreeMap(this.properties);
  }
}
