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

import org.jmxdatamart.Extractor.Setting.Settings;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.JMXTestServer.TestBean;
import java.util.Date;

public class Main {   
 
  public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            printHelp();
        }
        else {
            if (lookForHelp(args)){
                printHelp();                       
            }
            else{
                Long timeStart = new Long(System.currentTimeMillis());
                Long durationMilli = new Long(getRuntime(args));
                
                Settings s = Settings.fromXML(new FileInputStream(getConfig(args)));
        
                Extractor etor = new Extractor(s);
        
                Long sleeptime = s.getPollingRate()*1000;
                Long timeEnd = timeStart + durationMilli;
        
                while(timeEnd >= System.currentTimeMillis() )
                {
                    Thread.sleep(sleeptime);
                }
            }  
        }
  }
  public static void printHelp(){
      System.out.println("Extractor Syntax:");
      System.out.println("Extractor -h | h | ? | help , brings up this display");
      System.out.println("Extractor config.xml ##t , runs the extractor for ## time");
      System.out.println("    t must be either h (hours), m (minutes), s (seconds)");
      System.out.println("   ## must be an integer value");
      System.out.println("Example: Extractor s1.xml 60s (runs the extractor for 60 seconds)");
  }
  private static Long getRuntime(String[] argArray){
      //returns runTime in millisecs
      Long runTime = new Long(0);
      String timeUnit;
      for (int i = 0; i < argArray.length;i++)
      {
          timeUnit = argArray[i].substring(argArray[i].length()-1);
          
          if (timeUnit.equals("s")){
              runTime = stringToLong(argArray[i])*1000;
          }
          if (timeUnit.equals("m")){
              runTime = stringToLong(argArray[i])*60000;
          }
          if (timeUnit.equals("h")){
              runTime = stringToLong(argArray[i])*3600000;
          }
      }
      
      return runTime;
  }
  private static Long stringToLong(String num){
      // This method chops off the last char and tries to covert the rest to Long 
      //  returns 0 if error
      Long r = new Long(0);
      try {  
          r = Long.parseLong(num.substring(0, num.length()-1));
      }  
      catch(Exception e){ 
          r = (long)0;
      }  
      return r;    
  }
  private static String getConfig(String[] argArray){
      String extension;
      for(int i = 0; i<argArray.length; i++){
          extension = argArray[i].substring(argArray[i].length()-3);
          if (extension.equals("xml")){
              return argArray[i];
          }
      }
      return "";
  }
  private static Boolean lookForHelp(String[] argArray){
      for(int i = 0; i < argArray.length; i++){
          if (argArray[i].equals("-h") | argArray[i].equals("h") | argArray[i].equals("?") | argArray[i].equals("help")){
              return true;
          }
      }
      return false;
  }
}
// Old code for reference
/*
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
        
        int toBeExtracted = GET_A;
        
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
*/