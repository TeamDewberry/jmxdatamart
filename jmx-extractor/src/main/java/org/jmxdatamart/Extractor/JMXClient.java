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

    Settings configData = Settings.fromXML(new FileInputStream(System.getProperty("user.dir") + "/src/main/java/org/jmxdatamart/Extractor/settings.cfg"));
    FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/JMXClient.txt");
    writer.write("Start exporting on " + new java.util.Date()+"\n");

    JMXServiceURL url = null;
    JMXConnector jmxc = null;
    MBeanServerConnection mbsc = null;
    String output = null;
    try {
        url = new JMXServiceURL(configData.getUrl());
        jmxc = JMXConnectorFactory.connect(url, null);
        mbsc = jmxc.getMBeanServerConnection();

        while (true) {
            output = new Extractor(configData).extract();
            System.out.print(output);
            writer.write(output);
            writer.flush();
            Thread.sleep(configData.getPollingRate() * 1000);
        }
    } catch (InterruptedException e) {
      //MBeanInfo mBean = mbsc.getMBeanInfo(mBeanName);
    }
    catch (java.rmi.ConnectException e){
        System.err.println("Can't connect to Test Bean.");
        System.exit(1);
    }
    catch (IOException e){
        System.err.println("Can't connect to Test Server..");
        System.exit(1);
    }
    finally {
        writer.close();
        jmxc.close();
    }
  }

}
