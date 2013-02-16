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

package org.jmxdatamart.Extractor;

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.JMXTestServer.TestBean;

public class Main {
    
    static final int GET_A = 0x01;
    static final int GET_B = 0x02;
    static final int GET_BOTH = GET_A | GET_B;
    
    private static void getA(TestBean tb) throws Exception{
        Settings s = Settings.fromXML(
                new FileInputStream("C:\\Extracted\\s1.xml"));
        
        Extractor etor = new Extractor(s);
        
        for (int i = 0; i < 10; ++i){
            tb.setA(new Integer(i));
            Thread.sleep(2000);
        }
    }
    
    private static void getB(TestBean tb) throws Exception{
        Settings s = Settings.fromXML(
                new FileInputStream("C:\\Extracted\\s2.xml"));
        
        Extractor etor = new Extractor(s);
        
        for (int i = 100; i > 80; --i){
            tb.setB(new Integer(i));
            Thread.sleep(2000);
        }
    }
    
    private static void getBoth(TestBean tb) throws Exception{
        Settings s = Settings.fromXML(
                new FileInputStream("C:\\Extracted\\s3.xml"));
        
        Extractor etor = new Extractor(s);
        
        for (int i = 0; i < 15; ++i){
            tb.setA(i*i);
            tb.setB(100-2*i);
            Thread.sleep(2000);
        }
    }

  public static void main(String[] args) throws Exception {
        System.out.println("extract");
        
        // values
        int expected = 42;
        
        //Create new test MBean
        TestBean tb = new TestBean();
        tb.setA(new Integer(expected));
        tb.setB(new Integer(expected));
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String mbName = "org.jmxdatamart.JMXTestServer:type=TestBean";
        ObjectName mbeanName = new ObjectName(mbName);
        mbs.registerMBean(tb, mbeanName);
        
        // Main extract
        
        int toBeExtracted = GET_BOTH;
        
        switch (toBeExtracted){
            case GET_A:
                getA(tb);
                break;
            case GET_B:
                getB(tb);
                break;
            case GET_BOTH:
                getBoth(tb);
            default:
        }
  }
}
