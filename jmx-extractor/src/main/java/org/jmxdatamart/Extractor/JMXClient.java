/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor;

import java.io.*;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class JMXClient {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    // TODO code application logic here

    Settings configData = Settings.fromXML(new FileInputStream("settings.cfg"));

    JMXServiceURL url = new JMXServiceURL(configData.getUrl());
    JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
    MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

    try {
      while (true) {
        new Extractor(configData, mbsc).extract();
        Thread.sleep(configData.getPollingRate() * 1000);
      }

    } catch (InterruptedException e) {

      //MBeanInfo mBean = mbsc.getMBeanInfo(mBeanName);
      jmxc.close();
    }
  }

}
