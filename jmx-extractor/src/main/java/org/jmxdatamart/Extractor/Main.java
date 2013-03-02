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

public class Main {

  public static void main(String[] args) throws Exception {
    System.out.println("extract");

    Long timeStart = new Long(System.currentTimeMillis());
    Long durationMilli = new Long(Long.parseLong(args[0]) * 1000 * 60); //convert minutes to millisecs

    //System.out.println(durationMilli.toString());

    Settings s = Settings.fromXML(
            new FileInputStream("C:\\Extracted\\s1.xml"));

    Extractor etor = new Extractor(s);

    Long sleeptime = s.getPollingRate() * 1000;
    Long timeEnd = timeStart + durationMilli;

    while (timeEnd > System.currentTimeMillis()) {
      Thread.sleep(sleeptime);
    }

  }
//    private static final int GET_A = 0x01;
//    private static final int GET_B = 0x02;
//    private static final int GET_BOTH = GET_A | GET_B;
//    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MBeanExtract.class);
//    
//    private static void getA(TestBean tb) {
//        Settings s = null;
//		try {
//			s = Settings.fromXML(
//			        new FileInputStream("C:\\Extracted\\s1.xml"));
//		} catch (FileNotFoundException e) {
//			logger.error("Error while reading from settings file", e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		}
//        
//        Extractor etor = new Extractor(s);
//        
//        for (int i = 0; i < 10; ++i){
//            tb.setA(new Integer(i));
//            try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				logger.error(e.getMessage(), e);
//			}
//        }
//    }
//    
//    private static void getB(TestBean tb) {
//        Settings s = null;
//		try {
//			s = Settings.fromXML(
//			        new FileInputStream("C:\\Extracted\\s2.xml"));
//		} catch (FileNotFoundException e) {
//			logger.error("Error while reading from settings file", e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		}
//        
//        Extractor etor = new Extractor(s);
//        
//        for (int i = 100; i > 80; --i){
//            tb.setB(new Integer(i));
//            try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				logger.error(e.getMessage(), e);
//			}
//        }
//    }
//    
//    private static void getBoth(TestBean tb) {
//        Settings s = null;
//		try {
//			s = Settings.fromXML(
//			        new FileInputStream("C:\\Extracted\\s3.xml"));
//		} catch (FileNotFoundException e) {
//			logger.error("Error while reading from settings file", e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		}
//        
//		Extractor etor = new Extractor(s);
//        
//        for (int i = 0; i < 15; ++i){
//            tb.setA(i*i);
//            tb.setB(100-2*i);
//            try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				logger.error(e.getMessage(), e);
//			}
//        }
//    }
//
//  public static void main(String[] args) {
//        System.out.println("extract");
//        
//        // values
//        int expected = 42;
//        
//        //Create new test MBean
//        TestBean tb = new TestBean();
//        tb.setA(new Integer(expected));
//        tb.setB(new Integer(expected));
//        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//        String mbName = "org.jmxdatamart.JMXTestServer:type=TestBean";
//        ObjectName mbeanName = null;
//		try {
//			mbeanName = new ObjectName(mbName);
//		} catch (MalformedObjectNameException e) {
//			logger.error(e.getMessage(), e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		} catch (NullPointerException e) {
//			logger.error("Error creating MBean: no object name given", e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		}
//        try {
//			mbs.registerMBean(tb, mbeanName);
//		} catch (InstanceAlreadyExistsException e) {
//			logger.error("Error: " + mbeanName + " already registered with MBeanServer", e);
//		} catch (MBeanRegistrationException e) {
//			logger.error("Error registering " + mbeanName + " with MBeanServer", e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		} catch (NotCompliantMBeanException e) {
//			logger.error("Error: " + mbeanName + " is not compliant with MBeanServer", e);
//			System.exit(0); //this is a fatal error and cannot be resolved later
//		}
//        
//        // Main extract
//        
//        int toBeExtracted = GET_BOTH;
//        
//        switch (toBeExtracted){
//            case GET_A:
//                getA(tb);
//                break;
//            case GET_B:
//                getB(tb);
//                break;
//            case GET_BOTH:
//                getBoth(tb);
//            default:
//        }
//  }
}
