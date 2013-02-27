/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor.MXBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import org.jmxdatamart.Extractor.Attribute;
import org.jmxdatamart.common.DataType;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MultiLayeredAttribute {

  private List<String> layers;
  private MXNameParser mxnp;
  private MBeanServer mbs;
  private ObjectName baseMbean;
  private Attribute attribute;
  private String alias;
  private final org.slf4j.Logger logger = LoggerFactory.getLogger(MultiLayeredAttribute.class);

  public MultiLayeredAttribute(MBeanServer mbs) {
    mxnp = new MXNameParser();
    this.mbs = mbs;
  }

  public Map<Attribute, Object> getAll(ObjectName baseMbean, Attribute attr) {
    this.baseMbean = baseMbean;
    this.attribute = attr;
    if (attr.getAlias() != null && !attr.getAlias().isEmpty()) {
      this.alias = attr.getAlias();
    } else {
      this.alias = null;
    }
    parseName(this.attribute.getName());
    Map<Attribute, Object> resultSoFar = new HashMap<Attribute, Object>();
    try {
      for (MBeanAttributeInfo mbai : mbs.getMBeanInfo(this.baseMbean).getAttributes()) {
        if (mbai.getName().matches(layers.get(0))) {
          try {
            getAllHelper(
                    1,
                    layers.size(),
                    mbs.getAttribute(this.baseMbean, mbai.getName()),
                    mbai.getName() + ".",
                    resultSoFar);
          } catch (Exception ex) {
            logger.error("Error while trying to access " +
                    this.baseMbean.getCanonicalName() + " at " +
                    mbai.getName(), ex);
          }
        }
      }
    } catch (Exception ex) {
      logger.error("Error while trying to access " + this.baseMbean.getCanonicalName(), ex);
    }
    return resultSoFar;
  }

  private DataType getSupportedDataType(Object obj) {
    for (DataType dt : DataType.values()) {
      if (dt.supportTypeOf(obj)) {
        return dt;
      }
    }
    return null;
  }

  private void getAllHelper(
          int currDepth,
          int total,
          Object curr,
          String currName,
          Map<Attribute, Object> soFar) {
    if (curr == null) {
      logger.error("Null pointer in MX chain at " + currName);
      return;
    } else if (currDepth == total) {
      DataType dt = getSupportedDataType(curr);
      if (dt == null) {
        logger.error("Doesn't support type " + curr.getClass()
                + " from " + currName);
        return;
      } else {
        soFar.put(
                new Attribute(
                  null,
                    this.alias == null ?
                  name2alias(currName):
                  this.alias,
                  dt
                ),
                curr);
      }
    } else {
      if (CompositeData.class.isAssignableFrom(curr.getClass())) {
        CompositeData cd = (CompositeData) curr;
        for (String s : cd.getCompositeType().keySet()) {
          if (s.matches(layers.get(currDepth))) {
            getAllHelper(
                    currDepth + 1,
                    total,
                    cd.get(s),
                    currName + s + ".",
                    soFar);
          }
        }
      } else if (TabularData.class.isAssignableFrom(curr.getClass())) {
        TabularData td = (TabularData) curr;
        for (Object obj : td.keySet()) {
          List l = (List) obj;    // magic <-|- more magic
          for (Object o : l) {
            if (!String.class.isAssignableFrom(o.getClass())) {
              logger.error(o.getClass().toString() + " in "
                      + currName + " is not supported as Tabular key");
              continue;
            } else {
              String s = (String) o;
              if (s.matches(layers.get(currDepth))) {
                getAllHelper(
                        currDepth + 1,
                        total,
                        td.get(new Object[]{s}).get("value"), // magic -|-> more magic
                        currName + s + ".",
                        soFar);
              }
            }
          }
        }
      } else {
        logger.error("Doesn't support type " + curr.getClass()
                + " amid the MXBeanChain at " + currName.toString());
      }
    }
  }

  private boolean isPattern(String name) {
    return name.contains("?") || name.contains("*");
  }

  private String name2alias(CharSequence name) {
    StringBuilder sb = new StringBuilder(name.length());
    for (int i = 0; i < name.length(); ++i) {
      if (Character.isLetterOrDigit(name.charAt(i))) {
        sb.append(name.charAt(i));
      } else {
        sb.append('_');
      }
    }
    if (sb.charAt(sb.length() - 1) == '_') {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  private void parseName(String attrStr) throws IllegalArgumentException, RuntimeException {
    layers = mxnp.parse(attrStr);
    if (layers == null || layers.isEmpty()) {
      throw new RuntimeException("Can not parse " + attrStr);
    }
    for (int i = 0; i < layers.size(); ++i) {
      String temp = layers.get(i);
      if (isPattern(temp)) { // is *? pattern
        StringBuilder sb = new StringBuilder(temp.length());
        for (int j = 0; j < temp.length(); ++j) {
          switch (temp.charAt(j)) {   // turn into regex pattern
            case '.':
              sb.append("\\.");
              break;
            case '?':
              sb.append(".");
              break;
            case '*':
              sb.append(".*");
              break;
            default:
              sb.append(temp.charAt(j));
          }
        }
        layers.set(i, sb.toString());
      }
    }
  }
}
