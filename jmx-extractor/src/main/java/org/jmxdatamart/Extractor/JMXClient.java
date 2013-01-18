/*
 * Copyright (c) 2012, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
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
