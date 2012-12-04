/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.JMXTestServer;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

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
        while (true) {
            tb.randomize();
            System.out.println(tb.toString());
            Thread.sleep(2000);
        }
    }
}
