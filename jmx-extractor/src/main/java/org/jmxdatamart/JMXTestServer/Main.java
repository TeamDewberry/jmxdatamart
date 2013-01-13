/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.JMXTestServer;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.FileWriter;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        TestBean tb = new TestBean();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("com.personal.JMXTestServer:type=TestBean");
        mbs.registerMBean(tb, mbeanName);

        FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/JMXTestServer.txt");
        writer.write("Start exporting on " + new java.util.Date()+"\n");
        try {
            while (true) {
                tb.randomize();
                System.out.println(tb.toString());
                writer.write(tb.toString()+"\n");
                writer.flush();
                Thread.sleep(2000);
            }
        }
        catch (Exception e){
            System.err.println(e.getStackTrace());
            System.exit(1);
        }
        finally {
            writer.close();
        }
    }
}
