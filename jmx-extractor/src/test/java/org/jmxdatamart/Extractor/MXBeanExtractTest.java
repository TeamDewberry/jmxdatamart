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

import org.jmxdatamart.JMXTestServer.CarBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jmxdatamart.common.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matchers.*;
import org.hamcrest.core.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MXBeanExtractTest {

    /**
     * Test of extract method, of class MBeanExtract.
     */
    @Test
    public void testExtract() throws Exception {
        System.out.println("extract");
        ObjectName on = new ObjectName("org.jmxdatamart.JMXTestServer:type=CarBean");
        Object cb = new CarBean();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(cb, on);
        
        List<Attribute> attributes = new ArrayList<Attribute>();
        Attribute a1 = new Attribute("Car.name", "Full name", DataType.STRING);
        Attribute a2 = new Attribute("Car.year","Year unveiled", DataType.STRING);
        Attribute a3 = new Attribute("Car.engine", "Engine type", DataType.INT);
        Attribute a4 = new Attribute("Car.power", "Total power", DataType.INT);
        attributes.add(a1);
        attributes.add(a2);
        attributes.add(a3);
        attributes.add(a4);
        
        MBeanData mbd = new MBeanData(on.getCanonicalName(), "Dream Car", attributes, true);
        
        MBeanExtract mxbe = new MBeanExtract(mbd, mbs);
        
        Map<Attribute, Object> result = mxbe.extract();
 
        assertEquals(result.get(a1), CarBean.NAME);
        assertEquals(result.get(a2), CarBean.YEAR);
        assertEquals(result.get(a3), CarBean.ENGINE);
        assertEquals(result.get(a4), CarBean.POWER);
        assertEquals(result.size(), 4);
        
    }
}
