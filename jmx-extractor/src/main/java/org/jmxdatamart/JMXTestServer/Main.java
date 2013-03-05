/*
 * Copyright (c) 2012 Tripwire, Inc.
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

        TestBean tb1 = new TestBean();
        tb1.setA(new Integer(42));
        tb1.setB(new Integer(-1));
        ObjectName tbName1 = new ObjectName("com.personal.JMXTestServer:name=TestBean1");
        
        TestBean tb2 = new TestBean();
        tb2.setA(new Integer(55));
        tb2.setB(new Integer(-99));
        ObjectName tbName2 = new ObjectName("com.personal.JMXTestServer:name=TestBean2");
        
        CarBean cb = new CarBean();
        ObjectName cbName = new ObjectName("org.jmxdatamart:name=CarBean");
        
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(tb1, tbName1);
        mbs.registerMBean(cb, cbName);
        mbs.registerMBean(tb2, tbName2);
        
        if (args.length == 0) {
          System.out.println("Press Enter to terminate...");
          System.in.read();
        } else {  // a random argument will cause it to pause for 10s only
          Thread.sleep(10000);
        }
        
        mbs.unregisterMBean(tbName1);
        mbs.unregisterMBean(tbName2);
        mbs.unregisterMBean(cbName);
    }
}
