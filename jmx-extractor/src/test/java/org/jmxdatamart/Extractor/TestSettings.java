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

import java.util.Collections;
import junit.framework.TestCase;
import org.jmxdatamart.common.DataType;

public class TestSettings extends TestCase {

  public void testWriteSettingsAsXml() {
    ExtractorSettings settings = new ExtractorSettings();
    settings.setPollingRate(10);
    settings.setFolderLocation("jmx-statistics");

    MBeanData bean = new MBeanData();
    bean.setName("org.jmxdatamart:Type=TestWebAppMBean");
    bean.setAlias("TestWebAppMBean");

    Attribute attr = new Attribute("age", "age", DataType.INT);
    bean.setAttributes(Collections.singletonList(attr));

    settings.setBeans(Collections.singletonList(bean));

    String s = settings.toXML();
    
    ExtractorSettings newSettings = ExtractorSettings.fromXML(s);
    assertEquals(settings, newSettings);
  }
  
  public void testExpandSysProp() {
    ExtractorSettings s = new ExtractorSettings();
    
    String primum = "Primum non nocere - First, do no harm";
    assertEquals(primum, s.expandFromSystemProperties(primum));
    
    System.setProperty("ankel.pi", "3.14169");
    System.setProperty("ankel.ee", "2.71828");
    
    String str = s.expandFromSystemProperties("Area of a circle is ${ankel.pi} * radius * radius");
    assertEquals("Area of a circle is 3.14169 * radius * radius", str);
    
    str = s.expandFromSystemProperties("When radius is ${ankel.ee} then area is ${ankel.pi} * ${ankel.ee}^2");
    assertEquals("When radius is 2.71828 then area is 3.14169 * 2.71828^2", str);
    
    System.clearProperty("ankel.pi");
    System.clearProperty("ankel.ee");
  }
  
}
