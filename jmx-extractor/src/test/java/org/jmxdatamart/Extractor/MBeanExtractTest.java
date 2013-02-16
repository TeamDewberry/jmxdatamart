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

import org.jmxdatamart.JMXTestServer.TestBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.common.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MBeanExtractTest {
    
    public MBeanExtractTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of extract method, of class MBeanExtract.
     */
    @Test
    public void testExtract() throws Exception {
        System.out.println("extract");
        
        // values
        int expected = 42;
        
        //Create new test MBean
        TestBean tb = new TestBean();
        tb.setA(new Integer(expected));
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String mbName = "org.jmxdatamart.JMXTestServer:type=TestBean";
        ObjectName mbeanName = new ObjectName(mbName);
        mbs.registerMBean(tb, mbeanName);
        
        //Create test MBean's MBeanData
        Attribute a = new Attribute("A", "Alpha", DataType.INT);
        MBeanData mbd = new MBeanData(mbName, "testMBean",
                                        Collections.singletonList(a), true);
        
        //Init MBeanExtract
        MBeanExtract instance = new MBeanExtract(mbd, mbs);
        Map result = instance.extract();
        assertEquals(1, result.size());
        assertTrue(result.keySet().contains(a));
        assertEquals(expected, result.get(a));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
